package onlinemarket.converter;

import onlinemarket.model.Brand;
import onlinemarket.model.Product;
import onlinemarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ProductFormatter implements Formatter<Product> {

    @Autowired
    ProductService productService;

    @Override
    public Product parse(String s, Locale locale) {
        try {
            Integer id = Integer.parseInt(String.valueOf(s));
            return productService.getByKey(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String print(Product o, Locale locale) {
        if(o.getId() == null) return null;
        return Integer.toString(o.getId());
    }
}
