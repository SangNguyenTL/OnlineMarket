package OnlineMarket.controller.frontend;

import OnlineMarket.controller.MainController;
import OnlineMarket.form.filter.FilterForm;
import OnlineMarket.form.product.ProductDetail;
import OnlineMarket.model.Address;
import OnlineMarket.model.Order;
import OnlineMarket.model.OrderDetail;
import OnlineMarket.model.OrderDetailId;
import OnlineMarket.service.AddressService;
import OnlineMarket.service.ProductService;
import OnlineMarket.service.ProvinceService;
import OnlineMarket.util.exception.CustomException;
import OnlineMarket.util.other.ProductStatus;
import OnlineMarket.view.FrontendProduct;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/check-out")
public class FrCheckOutController extends MainController {

    @Autowired
    ProductService productService;

    @Autowired
    AddressService addressService;

    @Autowired
    ProvinceService provinceService;

    @Override
    public void addMeta(ModelMap modelMap){
        modelMap.put("pageTitle", "Checkout");
        generateBreadcrumbs();
        if(currentUser != null) {
            try {
                FilterForm form = new FilterForm();
                form.setOrderBy("province.name");
                form.setSize(null);
                modelMap.put("addressList", addressService.listByUser(currentUser, form));
                form.setOrderBy("name");
                form.setSize(null);
                modelMap.put("provinceList", provinceService.list(form));
            } catch (CustomException ignore) {

            }
        }
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
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
                    error.add("Product "+product.getName()+" is out of stock");
                }else if(product.getQuantity() == 0 || product.getState().equals(ProductStatus.OUTOFSTOCK.getId())){
                    error.add("Product "+product.getName()+" has stopped selling");
                }else {
                    if(10 < productDetail.getQuantity()) {
                        productDetail.setQuantity(10);
                        error.add("You only buy up to 10 items each");
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

}
