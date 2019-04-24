package com.evgeniradev.javassified.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="ads")
public class Ad {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String title;

	private String description;

	private Date createdAt;

	private Date updatedAt;
	
	@ManyToOne
	@JoinColumn(name="region_id")
	private Region region;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
  public Ad() {
    super();
  }
	
	public Ad(String title, String description) {
    this.title = title;
    this.description = description;
  }
	
  public Ad(String title, String description, Date createdAt, Date updatedAt, Region region, User user) {
    super();
    this.title = title;
    this.description = description;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.region = region;
    this.user = user;
  }

  public Ad(String title, String description, Date createdAt, Date updatedAt, Region region) {
    super();
    this.title = title;
    this.description = description;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.region = region;
  }

  public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getSlug() {
		return getId() + "-" + getTitle().trim().toLowerCase().replaceAll("\\W", "-");
	}

}
