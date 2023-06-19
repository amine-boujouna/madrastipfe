package com.sip.store.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Article {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @NotBlank(message = "Label is mandatory")
    @Column(name = "label")
    private String label;
    
    @NotBlank(message = "Description is mandatory")
    @Column(name = "description")
    private String description;
 
    @Column(name = "price")
    private float price;
    
    @NotBlank(message = "Face picture is mandatory")
    @Column(name = "facePicture")
    private String facePicture;
    
    @NotBlank(message = "Profile picture is mandatory")
    @Column(name = "profilePicture")
    private String profilePicture;

    @Column(name = "inventoryQuantity")
    private int inventoryQuantity;

    @Column(name = "promotionalPrice")
    private float promotionalPrice;
    
    @Column(name = "expiryDate")
    private String expiryDate;
    

    public Article() {}
    
	

	public Article(long id,String label, String description, float price,
			String facePicture, String profilePicture, int inventoryQuantity, float promotionalPrice, String expiryDate) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
		this.price = price;
		this.facePicture = facePicture;
		this.profilePicture = profilePicture;
		this.inventoryQuantity = inventoryQuantity;
		this.promotionalPrice = promotionalPrice;
		this.expiryDate = expiryDate;
	}



	@Override
	public String toString() {
		return "Article [id=" + id + ", label=" + label + ", description=" + description + ", price=" + price
				+ ", facePicture=" + facePicture + ", profilePicture=" + profilePicture + ", inventoryQuantity="
				+ inventoryQuantity + ", promotionalPrice=" + promotionalPrice + ", expiryDate=" + expiryDate
				 + "]";
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getFacePicture() {
		return facePicture;
	}

	public void setFacePicture(String facePicture) {
		this.facePicture = facePicture;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	/**** Many To One ****/
    @JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Provider provider;
	
	
	public Provider getProvider() {
    	return provider;
    }
    
    public void setProvider(Provider provider) {
    	this.provider=provider;
    }

	public int getInventoryQuantity() {
		return inventoryQuantity;
	}

	public void setInventoryQuantity(int inventoryQuantity) {
		this.inventoryQuantity = inventoryQuantity;
	}

	public float getPromotionalPrice() {
		return promotionalPrice;
	}

	public void setPromotionalPrice(float promotionalPrice) {
		this.promotionalPrice = promotionalPrice;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}  
}
