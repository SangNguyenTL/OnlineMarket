package onlinemarket.dao;

import org.springframework.stereotype.Repository;

import onlinemarket.model.Image;

@Repository("imageDao")
public class ImageDaoImpl extends AbstractDao<Integer, Image> implements ImageDao{


}
