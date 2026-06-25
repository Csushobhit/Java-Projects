package com.booking.appointment_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff extends User{
	
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@Column(name = "Last_name", nullable = false)
	private String lastName;

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
