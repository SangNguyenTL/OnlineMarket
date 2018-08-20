package OnlineMarket.converter;

import OnlineMarket.model.ProductCategory;
import org.springframework.format.Formatter;

import java.util.Locale;

public class ProductCategoryFormatter implements Formatter<ProductCategory> {
    @Override
    public ProductCategory parse(String s, Locale locale) {
        try {
            Integer id = Integer.parseInt(String.valueOf(s));
            return new ProductCategory(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String print(ProductCategory o, Locale locale) {
        if(o == null) return null;
        return Integer.toString(o.getId());
    }
}
