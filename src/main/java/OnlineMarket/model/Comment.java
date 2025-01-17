package OnlineMarket.model;

import OnlineMarket.util.group.AdvancedValidation;
import org.hibernate.validator.constraints.NotEmpty;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Comment generated by hbm2java
 */
@Entity
@Table(name = "tb_comment", schema = "dbo", catalog = "SmartMarket")
public class Comment implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Comment comment;
	private User user;
	private String content;
	private byte status;
	private Date updateDate;
	private Date createDate;
	private Set<Comment> comments = new HashSet<>(0);
	private Post post;

	public Comment() {
	}

	public Comment(Comment comment, User user, String content, byte status, Date createDate) {
		this.comment = comment;
		this.user = user;
		this.content = content;
		this.status = status;
		this.createDate = createDate;
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
	@JoinColumn(name = "parent_id", nullable = false)
	public Comment getComment() {
		return this.comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
    @NotNull
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "content", nullable = false)
    @Size(max = 1000, min = 10)
    @NotEmpty(groups = AdvancedValidation.AddNew.class)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "status", nullable = false)
	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date", length = 23)
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", nullable = false, length = 23)
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "comment")
	public Set<Comment> getComments() {
		return this.comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}


	@ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "post_id", nullable = false)
	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}
}
