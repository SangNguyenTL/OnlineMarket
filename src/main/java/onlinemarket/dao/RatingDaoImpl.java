package onlinemarket.dao;


import onlinemarket.model.Rating;
import org.springframework.stereotype.Repository;

@Repository("ratingDao")
public class RatingDaoImpl extends AbstractDao<Integer, Rating> implements RatingDao {

}
