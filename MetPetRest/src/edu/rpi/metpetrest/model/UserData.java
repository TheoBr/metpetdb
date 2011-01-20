package edu.rpi.metpetrest.model;

public class UserData {

	private Long userId;
	private String name;
	private String institution;
	private String professionalUrl;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getProfessionalUrl() {
		return professionalUrl;
	}

	public void setProfessionalUrl(String professionalUrl) {
		this.professionalUrl = professionalUrl;
	}

}
