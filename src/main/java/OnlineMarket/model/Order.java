package OnlineMarket.model;

import OnlineMarket.form.filter.OrderForm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tb_order", schema = "dbo", catalog = "SmartMarket")
public class Order implements java.io.Serializable {

	protected static final long serialVersionUID = 1L;
    protected Integer id;
    protected User user;
    protected Address address;
    protected Long totalPaid = 0L;
    protected Long totalPaidReal = 0L;
    protected Integer totalProduct = 0;
    protected Long totalShipping = 0L;
    protected Byte status;
    protected Date invoiceDate;
    protected Date createDate;
    protected Date updateDate;
    protected Set<OrderDetail> orderDetails = new HashSet<>(0);
    protected Set<Event> events = new HashSet<>(0);

	public Order() {
	}

    public Order(OrderForm orderForm) {
        for (Method getMethod : orderForm.getClass().getSuperclass().getMethods()) {
            if (getMethod.getName().startsWith("get")) {
                try {
                    Method setMethod = this.getClass().getMethod(getMethod.getName().replace("get", "set"), getMethod.getReturnType());
                    setMethod.invoke(this, getMethod.invoke(orderForm, (Object[]) null));

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {

                }
            }
        }
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL)
	public Set<OrderDetail> getOrderDetails() {
		return this.orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "tb_event_order", schema = "dbo", catalog = "SmartMarket", joinColumns = {
			@JoinColumn(name = "order_id", nullable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "event_id", nullable = false, updatable = false) })
	public Set<Event> getEvents() {
		return this.events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(user, order.user) &&
                Objects.equals(totalPaidReal, order.totalPaidReal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user, totalPaidReal);
    }
}
