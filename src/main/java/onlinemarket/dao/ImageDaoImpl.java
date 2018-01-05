package onlinemarket.dao;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import onlinemarket.api.result.ResultImage;
import onlinemarket.form.filter.ImageFilter;
import onlinemarket.model.Image;

@Repository("imageDao")
public class ImageDaoImpl extends AbstractDao<Integer, Image> implements ImageDao{

	@SuppressWarnings("unchecked")
	@Override
	public ResultImage filter(ImageFilter filter) {
		ResultImage result = new ResultImage();
		Criteria criteria = createEntityCriteria();
		
		if(StringUtils.isNotBlank(filter.getUploadType())) 
			criteria.add(Restrictions.eq("dataType", filter.getUploadType()));
		criteria.setProjection(Projections.rowCount());
		if(filter.getDatetime() != null && filter.getDateType() != null) {
		
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			
			
			switch (filter.getDateType()) {
			case "day":
				criteria.add(Restrictions.like("upload_date", sdf.format(filter.getDatetime())));
				break;
			case "week":
				c.setTime(filter.getDatetime());
				c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
				String startDate = sdf.format(c.getTime());
				c.add(Calendar.DAY_OF_WEEK, 7);
				String endDate = sdf.format(c.getTime());
				criteria.add(Restrictions.ge("upload_date", startDate));
				criteria.add(Restrictions.le("upload_date", endDate));
			case "month":
				sdf.applyPattern("yyyy-MM");
				criteria.add(Restrictions.like("upload_date", sdf.format(filter.getDatetime())));
			case "year":
				sdf.applyPattern("yyyy");
				criteria.add(Restrictions.like("upload_date", sdf.format(filter.getDatetime())));
			default:
				break;
			}

		}
			
		Long count = (Long) criteria.uniqueResult();
		result.setTotalRow(count);
		result.setCurrentPage(filter.getPageNumber());
		
		if(filter.getPageSize() > 0 && filter.getPageNumber() > 0) 
			criteria.setFirstResult((filter.getPageNumber()- 1) * filter.getPageSize()).setMaxResults(filter.getPageSize());
		criteria.setProjection(null);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Image> list = criteria.list();
		result.setList(list);
		return result;
	}


}
