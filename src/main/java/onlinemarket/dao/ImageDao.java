package onlinemarket.dao;

import onlinemarket.form.filter.ImageFilter;
import onlinemarket.model.Image;
import onlinemarket.result.api.ResultImage;

public interface ImageDao extends InterfaceDao<Integer, Image> {
	
	ResultImage filter(ImageFilter filter);
}
