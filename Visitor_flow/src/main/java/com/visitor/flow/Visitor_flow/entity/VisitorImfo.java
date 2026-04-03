package com.visitor.flow.Visitor_flow.entity;

import jakarta.persistence.Id;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name="visitors")
public class VisitorImfo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String department;
	private String purpose;
	private Long phone;
	private int age;
	private String email;
	private String gender;
	private String address;
	 private LocalDateTime dueDate;
	
	
	 public LocalDateTime getDueDate() {
		return dueDate;
	}


	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

  @Column
	private String status = "PENDING";
	    private LocalDateTime checkInTime;
	    private LocalDateTime checkOutTime;




	@Override
		public String toString() {
			return "VisitorImfo [id=" + id + ", name=" + name + ", department=" + department + ", purpose=" + purpose
					+ ", phone=" + phone + ", age=" + age + ", email=" + email + ", gender=" + gender + ", address="
					+ address + ", dueDate=" + dueDate + ", status=" + status + ", checkInTime=" + checkInTime
					+ ", checkOutTime=" + checkOutTime + "]";
		}


	public VisitorImfo() {
		super();
		// TODO Auto-generated constructor stub
	}


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


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getPurpose() {
		return purpose;
	}


	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}


	public Long getPhone() {
		return phone;
	}


	public void setPhone(Long phone) {
		this.phone = phone;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}


	public void setCheckInTime(LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
	}


	public LocalDateTime getCheckOutTime() {
		return checkOutTime;
	}


	public void setCheckOutTime(LocalDateTime checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	
	


}
