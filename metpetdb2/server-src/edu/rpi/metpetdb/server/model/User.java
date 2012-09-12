package edu.rpi.metpetdb.server.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Type;



@Entity
@Table(name="users")
public class User implements Serializable {

	private Long userId;
	private Integer version;
	private String emailAddress;
	private String name;
	private String address;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private String institution;
	private String referenceEmail;
	private Boolean enabled;
	private transient String confirmationCode;
	private Set<Sample> samples; 
	/*  * private Set<Project> projects; 
	 * private Set<Project> invites; */
	
	private Set<Role> roles = new HashSet<Role>();
	 
	private transient byte[] encryptedPassword;

	public User()
	{
		
	}
	
	public User(Long id, int version, String emailAddress, String name,
			String address, String city, String province, String country,
			String postalCode, String institution, String referenceEmail,
			Boolean enabled, String confirmationCode, byte[] encryptedPassword) {
		super();
		this.userId = id;
		this.version = version;
		this.emailAddress = emailAddress;
		this.name = name;
		this.address = address;
		this.city = city;
		this.province = province;
		this.country = country;
		this.postalCode = postalCode;
		this.institution = institution;
		this.referenceEmail = referenceEmail;
		this.enabled = enabled;
		this.confirmationCode = confirmationCode;
		this.encryptedPassword = encryptedPassword;
	}

	@SequenceGenerator(sequenceName="user_seq", name = "generator")
	@Id 
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="generator" )
	   
	public Long getUser_id() {
		return userId;
	}

	public void setUser_id(Long id) {
		this.userId = id;
	}

    @Version
    @Column(name="version")
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Column(name="email", nullable=false, length=255)
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	@Column(name="name", nullable=false, length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="address", nullable=true, length=200)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="city", nullable=true, length=50)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="province", nullable=true, length=100)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name="country", nullable=true, length=100)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name="postal_code", nullable=true, length=15)
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	@Column(name="institution", nullable=false, length=300)
	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	@Column(name="reference_email", nullable=true, length=255)
	public String getReferenceEmail() {
		return referenceEmail;
	}

	public void setReferenceEmail(String referenceEmail) {
		this.referenceEmail = referenceEmail;
	}

	@Column(name="enabled", nullable=true, length=1)
	@Type(type="yes_no")
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name="confirmation_code", nullable=true, length=32)
	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	@Column(name="password", nullable=false)	
	@Lob
	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(byte[] encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	@ManyToMany(fetch=FetchType.LAZY, targetEntity=edu.rpi.metpetdb.server.model.Role.class, cascade={CascadeType.ALL})

	@JoinTable(
        name="users_roles",
        joinColumns=@JoinColumn(name="user_id"),
        inverseJoinColumns=@JoinColumn(name="role_id")
        )
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public void addRole(Role role)
	{
		this.roles.add(role);
	}
	

	

}
