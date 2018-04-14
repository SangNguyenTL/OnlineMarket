package onlinemarket.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_order", schema = "dbo", catalog = "SmartMarket")
public class Order implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private User user;
	private Address address;
	private Long totalPaid;
	private Long totalPaidReal;
	private Integer totalProduct;
	private Integer totalShipping;
	private Byte status;
	private Date invoiceDate;
	private Date createDate;
	private Date updateDate;
	private Set<OrderDetail> orderDetails = new HashSet<>(0);

	public Order() {
	}

	public Order(User user) {
		this.user = user;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "_id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Column(name = "total_paid", precision = 13, scale = 0)
	public Long getTotalPaid() {
		return this.totalPaid;
	}

	public void setTotalPaid(Long totalPaid) {
		this.totalPaid = totalPaid;
	}

	@Column(name = "total_paid_real", precision = 13, scale = 0)
	public Long getTotalPaidReal() {
		return this.totalPaidReal;
	}

	public void setTotalPaidReal(Long totalPaidReal) {
		this.totalPaidReal = totalPaidReal;
	}

	@Column(name = "total_product")
	public Integer getTotalProduct() {
		return this.totalProduct;
	}

	public void setTotalProduct(Integer totalProduct) {
		this.totalProduct = totalProduct;
	}

	@Column(name = "total_shipping")
	public Integer getTotalShipping() {
		return this.totalShipping;
	}

	public void setTotalShipping(Integer totalShipping) {
		this.totalShipping = totalShipping;
	}

	@Column(name = "status")
	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "invoice_date", length = 23)
	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", length = 23)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", length = 23)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
	public Set<OrderDetail> getOrderDetails() {
		return this.orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

}
