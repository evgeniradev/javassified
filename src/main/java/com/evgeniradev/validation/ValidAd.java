package com.evgeniradev.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.evgeniradev.javassified.entity.Region;

public class ValidAd {
  
  @NotNull
  @Size(min = 10, message = "Title must be at least 10 characters long")
  @Size(max = 60, message = "Title must not be longer than 60 characters")
  private String title;
  
  @NotNull
  @Size(min = 30, message = "Description must be at least 30 characters long")
  @Size(max = 1000, message = "Description must not be longer than 1000 characters")
  private String description;
  
  @NotNull(message = "Region must be selected")
  private Region region;

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
 
}
