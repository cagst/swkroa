package com.cagst.swkroa.model;

/**
 * The Identification model for the Registration process.
 *
 * @author Craig Gaskill
 */
public class RegistrationIdentification {
  private String ownerId;
  private String firstName;
  private String lastName;

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

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
