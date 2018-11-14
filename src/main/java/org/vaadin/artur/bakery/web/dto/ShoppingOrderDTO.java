package org.vaadin.artur.bakery.web.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.vaadin.artur.bakery.domain.enumeration.OrderExtras;

public class ShoppingOrderDTO {

	private int version;
	private Long id;

	@NotBlank
	@Size(max = 255)
	private String customerName;

	@NotEmpty
	@Email
	@Size(max = 255)
	private String customerEmail;

	private boolean isCompany;

	@NotBlank
	@Size(max = 20, message = "{bakery.phone.number.invalid}")
	// A simple (Finnish) VAT id checker
	@Pattern(regexp = "^FI(\\d){8}$", message = "{bakery.phone.number.invalid}")
	private String vatID;

	@NotEmpty
	private Set<OrderExtras> extras = new HashSet<>();

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Set<OrderExtras> getExtras() {
		return extras;
	}

	public void setExtras(Set<OrderExtras> extras) {
		this.extras = extras;
	}

	public String getVatID() {
		return vatID;
	}

	public void setVatID(String vatID) {
		this.vatID = vatID;
	}

	public boolean isCompany() {
		return isCompany;
	}

	public void setCompany(boolean isCompany) {
		this.isCompany = isCompany;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
