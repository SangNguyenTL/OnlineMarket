package OnlineMarket.model;
// default package
// Generated Jan 2, 2018 4:57:38 PM by Hibernate Tools 4.3.5.Final

import OnlineMarket.util.other.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tb_role", schema = "dbo", catalog = "SmartMarket")
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name = RoleType.USER.getCode();
	private Set<User> users = new HashSet<>(0);

	public Role() {
	}

	public Role(Integer id){
	    this.id = id;
    }

	public Role(String name) {
		this.name = name;
	}

	public Role(String name, Set<User> users) {
		this.name = name;
		this.users = users;
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

	@Column(name = "[name]", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	@JsonIgnore
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
