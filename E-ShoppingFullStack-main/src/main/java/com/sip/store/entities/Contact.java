package com.sip.store.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Contact {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @NotBlank(message = "Phone is mandatory")
    @Column(name = "phone")
    private String phone;
    
    @NotBlank(message = "Fax is mandatory")
    @Column(name = "fax")
    private String fax;
    
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email")
    private String email;
    
    @NotBlank(message = "localisationn is mandatory")
    @Column(name = "localisationn")
    private String localisationn;

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocalisatiin() {
		return localisationn;
	}

	public void setLocalisatiin(String localisationn) {
		this.localisationn = localisationn;
	}

	public Contact(long id, @NotBlank(message = "Phone is mandatory") String phone,
			@NotBlank(message = "Fax is mandatory") String fax, @NotBlank(message = "Email is mandatory") String email,
			@NotBlank(message = "Localisatiin is mandatory") String localisationn) {
		super();
		this.id = id;
		this.phone = phone;
		this.fax = fax;
		this.email = email;
		this.localisationn = localisationn;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", phone=" + phone + ", fax=" + fax + ", email=" + email + ", localisationn="
				+ localisationn + "]";
	}
}
