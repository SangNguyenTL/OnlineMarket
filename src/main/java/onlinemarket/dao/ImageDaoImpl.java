package onlinemarket.dao;


import java.util.Calendar;
import java.util.Date;
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
		Date startDate, endDate;
		if(filter.getUploadType() != null && filter.getUploadType().length > 0) 
			criteria.add(Restrictions.in("dataType", filter.getUploadType()));
		criteria.setProjection(Projections.rowCount());
		if(filter.getDatetime() != null && StringUtils.isNotBlank(filter.getDateType())) {
		
			Calendar c = Calendar.getInstance();
			c.setTime(filter.getDatetime());
			switch (filter.getDateType()) {
			case "day":
		        c.set(Calendar.HOUR, 0);
		        c.set(Calendar.MINUTE, 0);
		        c.set(Calendar.SECOND, 0);
				startDate = c.getTime();
				c.set(Calendar.HOUR, 23);
		        c.set(Calendar.MINUTE, 59);
		        c.set(Calendar.SECOND, 59);
		        c.set(Calendar.MILLISECOND, 99);
				endDate = c.getTime();
				criteria.add(Restrictions.ge("uploadDate", startDate));
				criteria.add(Restrictions.le("uploadDate", endDate));
				break;
			case "week":
				c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
				startDate = c.getTime();
				c.add(Calendar.DAY_OF_WEEK, 7);
				endDate = c.getTime();
				criteria.add(Restrictions.ge("uploadDate", startDate));
				criteria.add(Restrictions.le("uploadDate", endDate));
				break;
			case "month":
				c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
				startDate = c.getTime();
				c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
				endDate = c.getTime();
				criteria.add(Restrictions.ge("uploadDate", startDate));
				criteria.add(Restrictions.le("uploadDate", endDate));
				break;
			case "year":
				c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
				startDate = c.getTime();
				c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
				endDate = c.getTime();
				criteria.add(Restrictions.ge("uploadDate", startDate));
				criteria.add(Restrictions.le("uploadDate", endDate));
				break;
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
