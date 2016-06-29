package com.cagst.swkroa.model;

/**
 * A model that contains information needed to register a Member.
 *
 * @author Craig Gaskill
 */
public class RegisterModel {
  private String stage;
  private String ownerId;
  private String firstName;
  private String lastName;

  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public  void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
