package edu.rpi.metpetdb.server.model;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

public class User extends MObject {
	private static final long serialVersionUID = 1L;

	private int id;
	private int version;

	private String emailAddress;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String firstName;
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String lastName;
	private String address;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private String institution;
	private String referenceEmail;
	
	private Set<Project> projects;
	@ContainedIn
	private Set<Sample> samples;
	
	private transient byte[] encryptedPassword;

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
		emailAddress = n;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
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

	public String getReferenceEmail() {
		return referenceEmail;
	}

	public void setReferenceEmail(final String referenceEmail) {
		this.referenceEmail = referenceEmail;
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

	public int hashCode() {
		return id;
	}

	public boolean equals(final Object o) {
		return o instanceof User && id == ((User) o).id;
	}

	public String toString() {
		return firstName + " " + lastName;
	}

	public boolean mIsNew() {
		return id == 0;
	}
}
