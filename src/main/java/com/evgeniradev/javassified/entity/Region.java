package com.evgeniradev.javassified.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="regions")
public class Region {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String name;

	@OneToMany(mappedBy="region")
	private List<Ad> ads;

	 public Region() {
	    super();
	 }
	
	public Region(String name) {
    this.name = name;
  }

  public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}

	public String getSlug() {
		return getName().trim().toLowerCase().replaceAll("\\W", "-");
	}

}
