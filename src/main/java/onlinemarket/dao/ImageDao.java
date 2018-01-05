package onlinemarket.dao;

import onlinemarket.form.filter.ImageFilter;
import onlinemarket.api.result.ResultImage;
import onlinemarket.model.Image;

public interface ImageDao extends InterfaceDao<Integer, Image> {
	
	ResultImage filter(ImageFilter filter);
}
