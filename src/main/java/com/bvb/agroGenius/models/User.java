package com.bvb.agroGenius.models;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue
	private Integer id;
	private String fullName;
	private String emailId;
	private String phoneNumber;
	private String gender;
	
	private String area;
	private String city;
	private String pincode;
	
	private String profilePic;
	
	@Column(name = "password", length = 64, nullable = false)
	private String password;
	
	@Column(name = "is_google_user")
	private Boolean isGoogleUser;
	
	private boolean enabled;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime lastModifiedAt;
	
	private Boolean isEmailVerified;
	
	private Boolean IsPhoneNumberVerified;
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", 
	joinColumns = {@JoinColumn(name = "user_id")}, 
	inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private Set<Role> roles = new HashSet<>();
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	public Boolean getIsEmailVerified() {
		return isEmailVerified;
	}

	public void setIsEmailVerified(Boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public Boolean getIsPhoneNumberVerified() {
		return IsPhoneNumberVerified;
	}

	public void setIsPhoneNumberVerified(Boolean isPhoneNumberVerified) {
		IsPhoneNumberVerified = isPhoneNumberVerified;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsGoogleUser() {
		return isGoogleUser;
	}

	public void setIsGoogleUser(Boolean isGoogleUser) {
		this.isGoogleUser = isGoogleUser;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", fullName=" + fullName + ", emailId=" + emailId + ", phoneNumber=" + phoneNumber
				+ ", gender=" + gender + ", area=" + area + ", city=" + city + ", pincode=" + pincode + ", profilePic="
				+ profilePic + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(area, city, emailId, fullName, gender, id, phoneNumber, pincode, profilePic);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(area, other.area) && Objects.equals(city, other.city)
				&& Objects.equals(emailId, other.emailId) && Objects.equals(fullName, other.fullName)
				&& Objects.equals(gender, other.gender) && Objects.equals(id, other.id)
				&& Objects.equals(phoneNumber, other.phoneNumber) && Objects.equals(pincode, other.pincode)
				&& Objects.equals(profilePic, other.profilePic);
	}	
}
