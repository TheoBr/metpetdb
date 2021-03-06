package edu.rpi.metpetdb.client.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
public class User extends MObject {
	private static final long serialVersionUID = 1L;

	@DocumentId
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private int id;
	private int version;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String emailAddress;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;
	private String address;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private String institution;
	private String professionalUrl;
	private Boolean enabled;
	private transient String confirmationCode;
	private Boolean contributorEnabled;
	private transient String contributorCode;
	
	private String researchInterests;
	private Boolean requestContributor;
	
	private Set<Project> projects;
	@ContainedIn
	private Set<Sample> samples;
	private Set<Project> invites;
	private Role role;
	private Integer rank;

	private transient byte[] encryptedPassword;
	
	public User()
	{
		super();
	}
	
	public User(String name)
	{
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(final int v) {
		id = v;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(final int v) {
		version = v;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String n) {
		this.emailAddress = n;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(final String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(final String country) {
		this.country = country;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(final String postalCode) {
		this.postalCode = postalCode;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(final String institution) {
		this.institution = institution;
	}

	public String getProfessionalUrl() {
		return professionalUrl;
	}

	public void setProfessionalUrl(final String professionalUrl) {
		this.professionalUrl = professionalUrl;
	}

	public byte[] getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(final byte[] p) {
		encryptedPassword = p;
	}

	public Set<Project> getProjects() {
		if (projects == null)
			projects = new HashSet<Project>();
		return projects;
	}

	public void setProjects(final Set<Project> c) {
		projects = c;
	}

	public Set<Sample> getSamples() {
		if (samples == null)
			samples = new HashSet<Sample>();
		return samples;
	}

	public void setSamples(final Set<Sample> s) {
		samples = s;
	}
	
		public Set<Project> getInvites() {
		if(invites == null)
			invites = new HashSet<Project>();
		return invites;
	}
	
	public void setInvites(final Set<Project> i) {
		invites = i;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		if (confirmationCode == null || confirmationCode.equals(""))
			return;

		this.confirmationCode = confirmationCode;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public int hashCode() {
		return id;
	}

	public boolean equals(final Object o) {
		return o instanceof User && id == ((User) o).id;
	}

	public String toString() {
		return name;
	}

	public boolean mIsNew() {
		return id == 0;
	}

	public Boolean getContributorEnabled() {
		return contributorEnabled;
	}

	public void setContributorEnabled(Boolean contributorEnabled) {
		this.contributorEnabled = contributorEnabled;
	}

	public String getContributorCode() {
		return contributorCode;
	}

	public void setContributorCode(String contributorCode) {
		if (contributorCode == null || contributorCode.equals(""))
			return;
		
		this.contributorCode = contributorCode;
	}
	
	public String getResearchInterests() {
		return researchInterests;
	}
	
	public void setResearchInterests(String researchInterests)
	{
		this.researchInterests = researchInterests;
	}

	public Boolean getRequestContributor() {
		return requestContributor;
	}

	public void setRequestContributor(Boolean requestContributor) {		
		this.requestContributor = requestContributor;
	}
	
	public boolean getUserAlreadyConfirmedAndNotRequested()
	{
		if (this.enabled)
			return true;
		else
			return false;
	}
	

}
