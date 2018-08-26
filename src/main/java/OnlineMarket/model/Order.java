package OnlineMarket.model;

import java.util.*;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tb_order", schema = "dbo", catalog = "SmartMarket")
public class Order implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private User user;
	private Address address;
	private Long totalPaid = 0L;
	private Long totalPaidReal = 0L;
	private Integer totalProduct = 0;
	private Long totalShipping = 0L;
	private Byte status;
	private Date invoiceDate;
	private Date createDate;
	private Date updateDate;
	private List<OrderDetail> orderDetails = new ArrayList<>(0);
	private List<Event> events = new ArrayList<>(0);

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
    @NotNull
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
	public Long getTotalShipping() {
		return this.totalShipping;
	}

	public void setTotalShipping(Long totalShipping) {
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
    @Valid
	public List<OrderDetail> getOrderDetails() {
		return this.orderDetails;
	}

	public void setOrderDetails(List<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_event_order", schema = "dbo", catalog = "SmartMarket", joinColumns = {
			@JoinColumn(name = "order_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "event_id", nullable = false, updatable = false) })
	public List<Event> getEvents() {
		return this.events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

}
