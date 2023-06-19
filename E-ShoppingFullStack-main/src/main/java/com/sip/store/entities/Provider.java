package com.sip.store.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

@Entity
public class Provider {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable=false)
    private String name;
    
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", nullable=false)
    private String email;

    public Provider() {}
    
    public Provider(long id, String name, String email, String phone, String address, String logo) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.logo = logo;
	}

	

	@Override
	public String toString() {
		return "Provider [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", address="
				+ address + ", logo=" + logo +  "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@NotBlank(message = "Phone is mandatory")
    @Column(name = "phone", nullable=false)
    private String phone;
    
    @NotBlank(message = "Address is mandatory")
    @Column(name = "address", nullable=false)
    private String address;

    @Column(name = "logo")
    private String logo;
    
   /* @OneToMany(cascade=CascadeType.ALL, mappedBy = "provider")
    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }*/
}
