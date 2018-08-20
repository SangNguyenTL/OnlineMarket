package OnlineMarket.model;
// default package
// Generated Jan 2, 2018 4:57:38 PM by Hibernate Tools 4.3.5.Final

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import OnlineMarket.form.product.ProductForm;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ReflectionUtils;

@Entity
@Table(name = "tb_product", schema = "dbo", catalog = "SmartMarket")
public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected Integer id;
	protected Brand brand;
	protected ProductCategory productCategory;
	protected User user;
	protected String name;
	protected String slug;
	protected String description;
	protected long price;
	protected Integer quantity;
	protected Integer numberOrder;
	protected Byte state;
	protected Integer weight;
	protected Date releaseDate;
	protected String size;
	protected Date createDate;
	protected Date updateDate;
	protected String featureImage;
	protected RatingStatistic ratingStatistic;
    protected ProductViewsStatistic productViewsStatistic;
	protected Set<Rating> ratings = new HashSet<>(0);
	protected Set<Event> events = new HashSet<>(0);
	protected List<ProductAttributeValues> productAttributeValues = new ArrayList<>();
	protected Set<ProductViews> productViewsSet = new HashSet<>(0);


	public Product() {}

    public Product(Integer id) {
		this.id = id;
    }

    public Product(ProductForm productForm){
        for (Method getMethod : this.getClass().getMethods()) {
            if (getMethod.getName().startsWith("get")) {
                try {
                    Method setMethod = this.getClass().getMethod(getMethod.getName().replace("get", "set"), getMethod.getReturnType());
                    setMethod.invoke(this, getMethod.invoke(productForm, (Object[]) null));

                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {

                }
            }
        }
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

	@ManyToOne(fetch = FetchType.EAGER)
	@NotNull
	@JoinColumn(name = "brand_id", nullable = false)
	@JsonBackReference
	public Brand getBrand() {
		return this.brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id", nullable = false)
	public ProductCategory getProductCategory() {
		return this.productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "publisher_id", nullable = false)
	@JsonIgnore
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "name", nullable = false)
	@Size(max = 255)
	@NotEmpty
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "slug", nullable = false)
	@Size(max = 255)
	@NotEmpty
	public String getSlug() {
		return this.slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

    @Column(name = "number_order")
    public Integer getNumberOrder() {
        return numberOrder;
    }

    public void setNumberOrder(Integer numberOrder) {
        this.numberOrder = numberOrder;
    }

	@Column(name = "description")
	@Size(max = 1000000)
	@JsonIgnore
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "price", nullable = false, precision = 13, scale = 6)
	@Range(max = 1000000000)
	public long getPrice() {
		return this.price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	@Column(name = "quantity", nullable = false)
	@Range(max = 10000)
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Column(name = "state", nullable = false)
	@Range(min = 0, max = 3)
	public Byte getState() {
		return this.state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	@Column(name = "create_date", nullable = false, length = 23)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss a")
	@Column(name = "update_date", length = 23)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "feature_image", nullable = false, length = 1024)
	@NotEmpty
	@Size(max = 2048)
	public String getFeatureImage() {
		return this.featureImage;
	}

	public void setFeatureImage(String featureImage) {
		this.featureImage = featureImage;
	}


	@Column(name = "weight", nullable = false)
	@Range(min = 0, max = 1000000)
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "MM/yyyy")
	@Column(name = "release_date", nullable = false, length = 23)
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Column(name = "size", nullable = false)
	@Size(min = 3, max = 150)
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@LazyCollection(LazyCollectionOption.EXTRA)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
	@Where(clause = "state = 'ACTIVE'")
	@JsonIgnore
	public Set<Rating> getRatings() {
		return this.ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}


    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonIgnore
    public RatingStatistic getRatingStatistic() {
        return ratingStatistic;
    }

    public void setRatingStatistic(RatingStatistic ratingStatistic) {
        this.ratingStatistic = ratingStatistic;
    }



	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinTable(name = "tb_event_product", schema = "dbo", catalog = "SmartMarket", joinColumns = {
			@JoinColumn(name = "product_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "event_id", nullable = false, updatable = false) })
	@Where(clause = "status = 0 AND (code is null OR code = '') AND GETDATE() BETWEEN date_from AND date_to")
	@LazyCollection(LazyCollectionOption.EXTRA)
	@JsonIgnore
	public Set<Event> getEvents() {
		return this.events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
	public List<ProductAttributeValues> getProductAttributeValues() {
		return productAttributeValues;
	}

	public void setProductAttributeValues(List<ProductAttributeValues> productAttributeValues) {
		this.productAttributeValues = productAttributeValues;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
	@JsonIgnore
	public Set<ProductViews> getProductViewsSet() {
		return this.productViewsSet;
	}

	public void setProductViewsSet(Set<ProductViews> productViewsSet) {
		this.productViewsSet = productViewsSet;
	}


    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonIgnore
    public ProductViewsStatistic getProductViewsStatistic() {
        return productViewsStatistic;
    }

    public void setProductViewsStatistic(ProductViewsStatistic productViewsStatistc) {
        this.productViewsStatistic = productViewsStatistc;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return Objects.equals(id, product.id) &&
				Objects.equals(slug, product.slug);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, slug);
	}

}
