package OnlineMarket.model;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import OnlineMarket.validation.PasswordsEqualConstraint;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import OnlineMarket.util.group.AdvancedValidation;
import OnlineMarket.validation.BeforeToday;
import OnlineMarket.validation.UniqueEmail;

@Entity
@Table(name = "tb_user", schema = "dbo", catalog = "SmartMarket", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@PasswordsEqualConstraint(groups = {AdvancedValidation.Register.class, AdvancedValidation.Advanced.class})
@UniqueEmail(groups= { AdvancedValidation.CheckEmail.class })
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Byte gender;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String confirmPassword;
	private boolean agree;
	private Date birthday;
	private Role role;
	private Date createDate;
	private Date updateDate;
	private String state;
	private String avatar = "/assets/images/defaultImage.jpg";
	private Set<Order> orders = new HashSet<>(0);
	private Set<Comment> comments = new HashSet<>(0);
	private Set<Rating> ratings = new HashSet<>(0);
	private List<Address> addresses = new ArrayList<>(0);
	private Set<Image> images = new HashSet<>(0);
	private Set<Notification> notifications = new HashSet<>(0);
	private Set<Post> posts = new HashSet<>(0);
	private Set<Product> products = new HashSet<>(0);
	private Set<Event> events = new HashSet<>(0);
	
	public User() {
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

	@Column(name = "gender", nullable = false)
	@Range(min=0, max=2, message = "Gender is invalid.")
	@NotNull
	public Byte getGender() {
		return this.gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	@Column(name = "first_name", nullable = false)
	@Size(max = 64)
	@NotEmpty
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name", nullable = false)
	@NotEmpty
	@Size(max = 64)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "email", unique = true, nullable = false, length = 128)
	@Size(min = 6, max=128)
	@Email
	@NotEmpty
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@NotEmpty(groups = AdvancedValidation.Register.class)
	@Size(min=6, max=60)
	@JsonIgnore
	@Column(name = "password", nullable = false, length = 60)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@BeforeToday
	@Column(name = "birthday", length = 23)
	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm a")
	@Column(name = "create_date", nullable = false, length = 23)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm a")
	@Column(name = "update_date", length = 23)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "state", nullable = false, length = 10)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "avatar", length = 1024)
	@Size(max=1024)
	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar == null ? this.avatar : avatar;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	@JsonIgnore
	public Set<Order> getOrders() {
		return this.orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Rating> getRatings() {
		return this.ratings;
	}

	public void setRatings(Set<Rating> ratings) {
		this.ratings = ratings;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Image> getImages() {
		return this.images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}
	
	@NotEmpty(groups = AdvancedValidation.Register.class)
    @Valid
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
    @Filter(name = "StatusUnRead")
	public Set<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(Set<Notification> notifications) {
		this.notifications = notifications;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Post> getPosts() {
		return this.posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Product> getProducts() {
		return this.products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable =  false)
	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "publisher")
	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(id, user.id) &&
				Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, email);
	}

	@Transient
	@NotEmpty(groups = AdvancedValidation.Register.class)
	@Size(min = 6, max = 60)
	@JsonIgnore
	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Transient
	@JsonIgnore
	@AssertTrue(groups = AdvancedValidation.Register.class)
	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	@Transient
	public String getDisplayName() {
		return firstName+" "+lastName;
	}

    public void merge(User entity) {
		for (Method getMethod : entity.getClass().getMethods()) {
			if (getMethod.getName().startsWith("get")) {
				try {
					Method setMethod = this.getClass().getMethod(getMethod.getName().replace("get", "set"), getMethod.getReturnType());
					Object object = getMethod.invoke(entity, (Object[]) null);
					if(object != null)
						setMethod.invoke(this, object);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {

				}
			}
		}
    }
}
