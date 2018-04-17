package onlinemarket.model;
// default package
// Generated Jan 2, 2018 4:57:38 PM by Hibernate Tools 4.3.5.Final

import java.util.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import onlinemarket.util.Help;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import onlinemarket.model.other.AdvancedValidation;
import onlinemarket.validation.UniqueProductSlug;

@Entity
@Table(name = "tb_product", schema = "dbo", catalog = "SmartMarket")
@UniqueProductSlug(groups = {AdvancedValidation.CheckSlug.class})
public class Product implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Brand brand;
	private ProductCategory productCategory;
	private User user;
	private String name;
	private String slug;
	private String beforeSlug;
	private String description;
	private long price;
	private String priceStr;
	private String newPriceStr;
	private Integer quantity;
	private Integer numberOrder;
	private Byte state;
	private Integer weight;
	private Date releaseDate;
	private String size;
	private Date createDate;
	private Date updateDate;
	private String featureImage;
	private RatingStatistic ratingStatistic;
    private ProductViewsStatistic productViewsStatistic;
	private Set<Rating> ratings = new HashSet<>(0);
	private Set<Event> events = new HashSet<>(0);
	private List<ProductAttributeValues> productAttributeValues = new ArrayList<>();
	private Set<ProductViews> productViewses = new HashSet<>(0);
	private Set<Comment> comments = new HashSet<>(0);
	private Set<Cart> carts = new HashSet<>(0);
	private Integer countAttribute = -1;
    private String sale;
    private boolean saleProcess = false;
    private long newPrice;

	public Product() {
	    newPrice = price;
	}

    public Product(Integer id) {
		this.id = id;
    }

    public void updateProduct(Product product){
		brand = product.brand;
		productCategory = product.productCategory;
		user = product.user;
		name = product.name;
		slug = product.slug;
		description = product.description;
		price = product.price;
		quantity = product.quantity;
		state = product.state;
		weight = product.weight;
		size = product.size;
		updateDate = new Date();
		featureImage = product.featureImage;
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
	@JsonIgnore
	@Filter(name ="ratingActive" ,condition = "state = 'Active'")
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
	@Filter(name = "active")
	@JoinTable(name = "tb_event_product", schema = "dbo", catalog = "SmartMarket", joinColumns = {
			@JoinColumn(name = "product_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "event_id", nullable = false, updatable = false) })
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
	public Set<ProductViews> getProductViewses() {
		return this.productViewses;
	}

	public void setProductViewses(Set<ProductViews> productViewses) {
		this.productViewses = productViewses;
	}


    @OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE)
    @JsonIgnore
    public ProductViewsStatistic getProductViewsStatistic() {
        return productViewsStatistic;
    }

    public void setProductViewsStatistic(ProductViewsStatistic productViewsStatistc) {
        this.productViewsStatistic = productViewsStatistc;
    }

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinTable(name = "tb_comment_product", schema = "dbo", catalog = "SmartMarket", joinColumns = {
			@JoinColumn(name = "product_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "comment_id", nullable = false, updatable = false) })
	@JsonIgnore
	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
	@JsonIgnore
	public Set<Cart> getCarts() {
		return this.carts;
	}

	public void setCarts(Set<Cart> carts) {
		this.carts = carts;
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

	@Transient
    @JsonIgnore
    public Integer getCountAttribute() {
        return countAttribute;
    }

    public void setCountAttribute(Integer countAttribute) {
        this.countAttribute = countAttribute;
    }

    public Integer processCount(){
	    return countAttribute++;
    }

    public boolean checkProductAttributes(AttributeValues attributeValues){
		ProductAttributeValues productAttributeValue = new ProductAttributeValues();
		productAttributeValue.setAttributeValuesId(attributeValues.getId());
		productAttributeValue.setProductId(this.getId());
		return productAttributeValues.contains(productAttributeValue);
	}

	@Transient
    @JsonIgnore
	public String getPriceStr() {
		return Help.format(price);
	}

	@Transient
    @JsonIgnore
	public String getNewPriceStr() {
        Integer perSale = 0;
        long value = 0;
        if(!saleProcess){
            for (Event event : events){
                if(!event.getDateFrom().before(new Date()) && !event.getDateTo().after(new Date())) continue;
                if(event.getPercentValue() != null && event.getMaxPrice() > price && event.getMinPrice() < price){
                    perSale += event.getPercentValue();
                }
                if(event.getValue() != null  && event.getMaxPrice() > price && event.getMinPrice() < price)
                    value += event.getValue();
            }
            Double number = Math.ceil(price - (price * perSale/100d) - value);
            newPrice = number.longValue();

            if(perSale != 0)
                sale = "-"+perSale.toString()+"%";
            else if(value != 0) sale = "-"+Help.format(value);
            else sale = "";
            saleProcess = true;
        }
		return Help.format(newPrice);
	}

    @Transient
    @JsonIgnore
    public String getSale() {
	    return  sale;
    }

    @Transient
    @JsonIgnore
    public boolean isSaleProcess() {
        return saleProcess;
    }

    @Transient
    @JsonIgnore
    public long getNewPrice() {
        return newPrice;
    }

    @Transient
    @JsonIgnore
    public String getBeforeSlug() {
        return beforeSlug;
    }

    public void setBeforeSlug(String beforeSlug) {
        this.beforeSlug = beforeSlug;
    }
}
