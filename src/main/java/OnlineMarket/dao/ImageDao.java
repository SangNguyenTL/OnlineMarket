package OnlineMarket.dao;

import OnlineMarket.form.filter.ImageFilter;
import OnlineMarket.model.Image;
import OnlineMarket.result.api.ResultImage;

public interface ImageDao extends InterfaceDao<Integer, Image> {
	
	ResultImage filter(ImageFilter filter);
}
