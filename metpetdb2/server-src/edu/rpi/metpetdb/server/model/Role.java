package edu.rpi.metpetdb.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role implements Serializable {

	private Long id;
	private String roleName;

	/* private List<RolePrivilege> rolePrivileges; */

	public Role() {

	}

	public Role(Long id, String roleName) {
		super();
		this.id = id;
		this.roleName = roleName;
	}

	@SequenceGenerator(sequenceName = "role_seq", name = "generator")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	public Long getRole_id() {
		return id;
	}

	public void setRole_id(Long id) {
		this.id = id;
	}

	@Column(name="role_name", nullable=false, length=50)
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
