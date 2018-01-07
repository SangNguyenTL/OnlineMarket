package onlinemarket.form.filter;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

public class ImageFilter {
	
	private int pageNumber;
	
	private int pageSize;
	
	private String[] uploadType;
	
	private String dateType;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yy-mm-dd")
	@Pattern(regexp = "(?:19|20)[0-9]{2}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-9])|(?:(?!02)(?:0[1-9]|1[0-2])-(?:30))|(?:(?:0[13578]|1[02])-31))")
	private Date datetime;

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String[] getUploadType() {
		return uploadType;
	}

	public void setUploadType(String[] uploadType) {
		this.uploadType = uploadType;
	}
	
}
