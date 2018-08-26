package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.form.product.ProductDetail;
import OnlineMarket.model.*;
import OnlineMarket.service.*;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.other.EventStatus;
import OnlineMarket.util.other.ProductStatus;
import OnlineMarket.view.FrontendProduct;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/check-out")
public class FrCheckOutController extends MainController {

    @Autowired
    ProductService productService;

    @Autowired
    AddressService addressService;

    @Autowired
    ProvinceService provinceService;

    @Autowired
    OrderService orderService;

    @Autowired
    EventService eventService;

    @Override
    public void addMeta(ModelMap modelMap){
        modelMap.put("pageTitle", "Checkout");
        generateBreadcrumbs();
        if(currentUser != null) {
            try {
                FilterForm form = new FilterForm();
                relativePath = "/check-out";
                form.setOrderBy("province.name");
                form.setSize(null);
                modelMap.put("addressList", addressService.listByUser(currentUser, form));
                form.setOrderBy("name");
                form.setSize(null);
                modelMap.put("provinceList", provinceService.list(form));
                modelMap.put("pathAction", relativePath);
            } catch (CustomException ignore) {

            }
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String mainPage(@CookieValue(value = "cart", required = false) String cart, ModelMap modelMap){

        if(currentUser == null){
            return "redirect:/register";
        }
        if(cart == null) cart = "";
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductDetail> productDetailList = new ArrayList<>();
        List<String> error = new ArrayList<>();
        try {
            productDetailList = objectMapper.readValue(cart, new TypeReference<List<ProductDetail>>(){});
        } catch (IOException ignore) {

        }
        Order order = new Order();
        long totalPaid = 0;
        long totalPaidReal = 0;
        long totalShipping = 0;
        long totalProduct = 0;
        long totalWeight = 0;


        if(productDetailList.size() > 0){
            for(ProductDetail productDetail : productDetailList){
                FrontendProduct product = productService.convertProductToFrProduct(productService.getByKey(productDetail.getId()));
                if(product == null) {
                    error.add("Product id "+productDetail.getId()+" does not found.");
                }else if(product.getState().equals(ProductStatus.LOCK.getId()) || product.getState().equals(ProductStatus.STOPSELLING.getId())){
                    error.add("Product "+product.getName()+" has stopped selling.");
                }else if(product.getQuantity() == 0 || product.getState().equals(ProductStatus.OUTOFSTOCK.getId())){
                    error.add("Product "+product.getName()+" is out of stock.");
                }else {
                    if(10 < productDetail.getQuantity()) {
                        productDetail.setQuantity(10);
                        error.add("You only buy up to 10 "+product.getName()+" each");
                    }else if( productDetail.getQuantity() > product.getQuantity() && productDetail.getQuantity() < 10){
                        productDetail.setQuantity(product.getQuantity());
                        error.add("Product "+product.getName()+"has only "+product.getQuantity()+" item(s)");
                    }
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(new OrderDetailId(productDetail.getId()));
                    orderDetail.setProduct(product);
                    orderDetail.setProductQuantity(productDetail.getQuantity());
                    order.getOrderDetails().add(orderDetail);
                    totalProduct = totalProduct + orderDetail.getProductQuantity();
                    totalWeight = totalWeight + product.getWeight() * orderDetail.getProductQuantity();
                    totalPaid = totalPaid + product.getPrice();
                    totalPaidReal = totalPaidReal + product.getNewPrice();
                    order.getEvents().addAll(product.getEvents());
                }
                if(order.getAddress() != null)
                    totalShipping = (long) (Math.floor((double) totalWeight/1000) * order.getAddress().getProvince().getShippingFee());
                order.setTotalPaid(totalPaid);
                order.setTotalPaidReal(totalPaidReal);
                order.setTotalShipping(totalShipping);

            }
        }
        modelMap.put("totalWeight", totalWeight);
        modelMap.put("order", order);
        modelMap.put("error", error);

        return "frontend/check-out";

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String confirmOrder(@Valid @ModelAttribute("order") Order order, BindingResult result,
                               RedirectAttributes redirectAttributes, ModelMap modelMap,
                               HttpServletRequest request,
                               HttpServletResponse response){
        if(currentUser == null){
            return "redirect:/register";
        }
        List<String> error = new ArrayList<>();
        long totalPaid = 0;
        long totalPaidReal = 0;
        long totalShipping = 0;
        int totalProduct = 0;
        long totalWeight = 0;
        long totalSale = 0;
        int countProductEvent = 0;
        Map<Integer, Event> eventForOrder = new HashMap<>();
        List<Event> eventList = new ArrayList<>(order.getEvents());
        order.getEvents().clear();
        if(order.getOrderDetails().size() > 0){
            for(int i = 0; i < order.getOrderDetails().size(); i++ ){
                OrderDetail orderDetail = order.getOrderDetails().get(i);
                if(orderDetail.getId() == null || orderDetail.getProductQuantity() == 0){
                    continue;
                }
                FrontendProduct product = productService.convertProductToFrProduct(productService.getByKey(orderDetail.getId().getProductId()));
                if(product == null) {
                    error.add("Product id "+orderDetail.getId().getProductId()+" does not found.");
                    order.getOrderDetails().remove(orderDetail);
                    continue;
                }else if(product.getState().equals(ProductStatus.LOCK.getId()) || product.getState().equals(ProductStatus.STOPSELLING.getId())){
                    FieldError objectError = new FieldError("order", "orderDetails["+i+"].product","This product has stop selling.");
                    result.addError(objectError);
                }else if(product.getQuantity() == 0 || product.getState().equals(ProductStatus.OUTOFSTOCK.getId())){
                    FieldError objectError = new FieldError("order", "orderDetails["+i+"].product","This product is out of stock.");
                    result.addError(objectError);
                }

                if(product != null){
                    if( orderDetail.getProductQuantity() > product.getQuantity()){
                        FieldError objectError = new FieldError("order","orderDetails["+i+"].productQuantity", "Product "+product.getName()+" has only "+product.getQuantity()+" item(s)");
                        result.addError(objectError);
                    }
                    for(int k = 0; k < eventList.size(); k++){
                        Event eventCheck = eventList.get(k);
                        if(eventCheck.getCode() == null) {
                            continue;
                        }
                        Event event = eventService.getByDeclaration("code", eventCheck.getCode());
                        if(order.getEvents().contains(event)) {
                            continue;
                        }
                        Calendar calendar = Calendar.getInstance();
                        if(event == null
                                || calendar.getTime().getTime() - event.getDateTo().getTime() > 0
                                ||  event.getDateFrom().getTime() - calendar.getTime().getTime() > 0
                                || !event.getStatus().equals(EventStatus.ACTIVE.getId())
                                || (event.getCount() != null && event.getCount() == 0)){
                            error.add(eventCheck.getCode()+ " is invalid");
                            continue;
                        }
                        for(Product productEvent : event.getProducts()){
                            if(productEvent.getId().equals(product.getId()) && product.getPrice() >= event.getMinPrice() && product.getPrice() <= event.getMaxPrice()){
                                countProductEvent++;
                                Set<Event> productEvents = new HashSet<>(product.getEvents());
                                productEvents.add(event);
                                productService.processEventProduct(product, productEvents);
                            }
                        }
                        if(countProductEvent == 0 && event.getProducts().size() > 0 ){
                            error.add(event.getCode()+ " is invalid");
                        }else if(event.getProducts().size() == 0){
                            eventForOrder.put(k, event);
                        }
                    }
                    totalProduct = totalProduct + orderDetail.getProductQuantity();
                    totalPaid = totalPaid + product.getPrice() * orderDetail.getProductQuantity();
                    totalProduct = totalProduct + orderDetail.getProductQuantity();
                    totalWeight = totalWeight + product.getWeight() * orderDetail.getProductQuantity();
                    totalPaidReal = totalPaidReal + product.getNewPrice() * orderDetail.getProductQuantity();

                    orderDetail.setProduct(product);
                    orderDetail.setProductName(product.getName());
                    orderDetail.setProductPrice(product.getPrice());
                    orderDetail.setOrder(order);
                    orderDetail.setProductWeight(product.getWeight());
                    order.getEvents().addAll(product.getEvents());

                    if(order.getAddress() != null)
                        totalShipping = (long) (Math.floor((double) totalWeight/1000) * order.getAddress().getProvince().getShippingFee());

                }
            }
            for(OrderDetail orderDetail : order.getOrderDetails()){
                if(orderDetail.getId() == null || orderDetail.getProductQuantity() == 0) order.getOrderDetails().remove(orderDetail);
            }

            order.setTotalPaid(totalPaid);
            order.setTotalPaidReal(totalPaidReal);
            order.setTotalShipping(totalShipping);
            order.setTotalProduct(totalProduct);
            for (Map.Entry<Integer, Event> eventEntry : eventForOrder.entrySet()) {
                Event event = eventEntry.getValue();
                if(order.getEvents().contains(event)) continue;
                if (totalPaid > event.getMaxPrice() || totalPaid < event.getMinPrice()) {
                    error.add(event.getCode()+ ": Order value is not in the price range of the code.");
                }else{
                    if (event.getValue() != null) {
                        totalSale = totalSale + event.getValue();
                    } else if (event.getPercentValue() != null) {
                        totalSale = totalSale + (int) Math.floor((double) totalPaidReal / 100 * event.getPercentValue());
                    }
                }

                order.getEvents().add(event);
            }
            order.setTotalPaidReal(totalPaidReal-totalSale);
            order.setUser(currentUser);
            modelMap.put("totalSale", totalSale);
        }
        if(!result.hasErrors()){
            orderService.save(order);
            for(Cookie cookie : request.getCookies()){
                if(cookie.getName().equals("cart")){
                    cookie.setValue("[]");
                    response.addCookie(cookie);
                }
            }
            redirectAttributes.addFlashAttribute("success", order);
            return "redirect:/check-out";
        }

        modelMap.put("totalWeight", totalWeight);
        modelMap.put("order", order);
        modelMap.put("error", error);
        modelMap.put("org.springframework.validation.BindingResult.comment", result);

        return "frontend/check-out";
    }

}
