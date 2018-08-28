package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.form.filter.FilterFormPrivate;
import OnlineMarket.form.filter.OrderForm;
import OnlineMarket.form.product.ProductDetail;
import OnlineMarket.listener.OnOrderEvent;
import OnlineMarket.model.*;
import OnlineMarket.service.*;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.other.EventStatus;
import OnlineMarket.util.other.ProductStatus;
import OnlineMarket.view.FrontendProduct;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public void addMeta(ModelMap modelMap){
        modelMap.put("pageTitle", "Checkout");
        generateBreadcrumbs();
        if(currentUser != null) {
            try {
                FilterForm form = new FilterFormPrivate();
                relativePath = "/check-out";
                form.setOrderBy("province.name");
                form.setSize(null);
                modelMap.put("addressList", addressService.listByUser(currentUser, form));
                form.setOrderBy("province.name");
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
        OrderForm order = new OrderForm();
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
                    if(productDetail.getQuantity() > 10) {
                        if(product.getQuantity() > 10){
                            productDetail.setQuantity(10);
                            error.add("You only buy up to 10 "+product.getName()+" each");
                        }else {
                            productDetail.setQuantity(product.getQuantity());
                            error.add("Product "+product.getName()+"has only "+product.getQuantity()+" item(s)");
                        }
                    }
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setId(new OrderDetailId(productDetail.getId()));
                    orderDetail.setProduct(product);
                    orderDetail.setProductQuantity(productDetail.getQuantity());
                    order.getOrderDetailList().add(orderDetail);
                    totalProduct = totalProduct + orderDetail.getProductQuantity();
                    totalWeight = totalWeight + product.getWeight() * orderDetail.getProductQuantity();
                    totalPaid = totalPaid + product.getPrice();
                    totalPaidReal = totalPaidReal + product.getNewPrice();
                    order.getEventList().addAll(product.getEvents());
                }
                if(order.getAddress() != null)
                    totalShipping = (long) (Math.floor((double) totalWeight/1000) * order.getAddress().getProvince().getShippingFee());
                order.setTotalPaid(totalPaid);
                order.setTotalPaidReal(totalPaidReal);
                order.setTotalShipping(totalShipping);
            }
        }
        modelMap.put("totalWeight", totalWeight);
        modelMap.put("orderForm", order);
        modelMap.put("error", error);

        return "frontend/check-out";

    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String confirmOrder(@Valid @ModelAttribute("orderForm") OrderForm orderForm, BindingResult result,
                               RedirectAttributes redirectAttributes, ModelMap modelMap,
                               HttpServletRequest request){
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
        Map<Integer, Event> eventForOrder = new HashMap<>();
        List<Event> eventList = new ArrayList<>(orderForm.getEventList());
        orderForm.getEventList().clear();
        if(orderForm.getOrderDetailList().size() > 0){
            for(int i = 0; i < orderForm.getOrderDetailList().size(); i++ ){
                OrderDetail orderDetail = orderForm.getOrderDetailList().get(i);
                if(orderDetail.getId() == null || orderDetail.getProductQuantity() == 0){
                    continue;
                }
                FrontendProduct product = productService.convertProductToFrProduct(productService.getByKey(orderDetail.getId().getProductId()));
                if(product == null) {
                    error.add("Product id "+orderDetail.getId().getProductId()+" does not found.");
                    orderForm.getOrderDetails().remove(orderDetail);
                    continue;
                }else if(product.getState().equals(ProductStatus.LOCK.getId()) || product.getState().equals(ProductStatus.STOPSELLING.getId())){
                    FieldError objectError = new FieldError("orderForm", "orderDetailList["+i+"].product","This product has stop selling.");
                    result.addError(objectError);
                }else if(product.getQuantity() == 0 || product.getState().equals(ProductStatus.OUTOFSTOCK.getId())){
                    FieldError objectError = new FieldError("orderForm", "orderDetailList["+i+"].product","This product is out of stock.");
                    result.addError(objectError);
                }

                if(product != null){
                    if( orderDetail.getProductQuantity() > product.getQuantity()){
                        FieldError objectError = new FieldError("orderForm","orderDetailList["+i+"].productQuantity", "Product "+product.getName()+" has only "+product.getQuantity()+" item(s)");
                        result.addError(objectError);
                    }
                    for(int k = 0; k < eventList.size(); k++){
                        Event eventCheck = eventList.get(k);
                        if(eventCheck == null ||    eventCheck.getCode() == null) {
                            continue;
                        }
                        Event event = eventService.getByDeclaration("code", eventCheck.getCode());
                        if(orderForm.getEventList().contains(event)) {
                            continue;
                        }
                        if(event == null){
                            error.add(eventCheck.getCode()+ " code is invalid.");
                            eventList.remove(eventCheck);
                            k--;
                            continue;
                        }
                        if(orderService.getByCodeAndUser(event.getCode(), currentUser) != null){
                            error.add(eventCheck.getCode()+ " code has been used by you.");
                            eventList.remove(eventCheck);
                            k--;
                            continue;
                        }
                        Calendar calendar = Calendar.getInstance();
                        if(calendar.getTime().getTime() - event.getDateTo().getTime() > 0
                                ||  event.getDateFrom().getTime() - calendar.getTime().getTime() > 0
                                || !event.getStatus().equals(EventStatus.ACTIVE.getId())
                                || (event.getCount() != null && event.getCount() == 0)){
                            error.add(eventCheck.getCode()+ " code is expired or out stock.");
                            eventList.remove(eventCheck);
                            k--;
                            continue;
                        }
                        for(Product productEvent : event.getProducts()){
                            if(productEvent.getId().equals(product.getId())){
                                if(product.getPrice() >= event.getMinPrice() && product.getPrice() <= event.getMaxPrice()){
                                    Set<Event> productEvents = new HashSet<>(product.getEvents());
                                    productEvents.add(event);
                                    productService.processEventProduct(product, productEvents);
                                }
                            }
                        }
                        if(event.getProducts().size() == 0){
                            eventForOrder.put(k, event);
                        }
                    }



                    totalPaid = totalPaid + product.getPrice() * orderDetail.getProductQuantity();
                    totalProduct = totalProduct + orderDetail.getProductQuantity();
                    totalWeight = totalWeight + product.getWeight() * orderDetail.getProductQuantity();
                    totalPaidReal = totalPaidReal + product.getNewPrice() * orderDetail.getProductQuantity();

                    orderDetail.setProduct(product);
                    orderDetail.setProductName(product.getName());
                    orderDetail.setProductPrice(product.getNewPrice());
                    orderDetail.setProductWeight(product.getWeight());
                    orderForm.getEventList().addAll(product.getEvents());

                }
            }
            for(OrderDetail orderDetail : orderForm.getOrderDetailList()){
                if(orderDetail.getId() == null || orderDetail.getProductQuantity() == 0) orderForm.getOrderDetailList().remove(orderDetail);
            }

            if(orderForm.getAddress() != null)
                totalShipping = (new Double((double) totalWeight/1000 * orderForm.getAddress().getProvince().getShippingFee())).longValue();

            orderForm.setTotalPaid(totalPaid);
            orderForm.setTotalPaidReal(totalPaidReal);
            orderForm.setTotalShipping(totalShipping);
            orderForm.setTotalProduct(totalProduct);
            for (Map.Entry<Integer, Event> eventEntry : eventForOrder.entrySet()) {
                Event event = eventEntry.getValue();
                if(orderForm.getEventList().contains(event)) continue;
                if (totalPaid > event.getMaxPrice() || totalPaid < event.getMinPrice()) {
                    error.add(event.getCode()+ ": Order value is not in the price range of the code.");
                }else{
                    if (event.getValue() != null) {
                        totalSale = totalSale + event.getValue();
                    } else if (event.getPercentValue() != null) {
                        totalSale = totalSale + (int) Math.floor((double) totalPaid / 100 * event.getPercentValue());
                    }
                }

                orderForm.getEventList().add(event);
            }
            orderForm.setTotalPaidReal(totalPaidReal-totalSale);
            orderForm.setUser(currentUser);
            modelMap.put("totalSale", totalSale);
        }
        if(!result.hasErrors() && error.size() == 0){
            Order order =  orderService.save(orderForm);
            redirectAttributes.addFlashAttribute("success", order.getId());
            eventPublisher.publishEvent(new OnOrderEvent(order, "submit", request));
            return "redirect:/check-out?check-out=success";
        }

        modelMap.put("totalWeight", totalWeight);
        modelMap.put("orderForm", orderForm);
        modelMap.put("error", error);
        modelMap.put("org.springframework.validation.BindingResult.comment", result);

        return "frontend/check-out";
    }

}
