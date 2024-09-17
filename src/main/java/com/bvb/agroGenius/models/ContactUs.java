package com.bvb.agroGenius.models;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "contact_us")
public class ContactUs {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String emailId;
	
	private String subject;
	
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment, emailId, id, name, subject);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactUs other = (ContactUs) obj;
		return Objects.equals(comment, other.comment) && Objects.equals(emailId, other.emailId)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(subject, other.subject);
	}
	
}
