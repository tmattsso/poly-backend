package org.vaadin.artur.bakery.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.vaadin.artur.bakery.domain.enumeration.OrderExtras;

@SuppressWarnings("serial")
@Entity
public class ShoppingOrder extends AbstractEntity {

	@NotBlank
	@Size(max = 255)
	private String customerName;

	@NotEmpty
	@Email
	@Size(max = 255)
	@Column(unique = true)
	private String customerEmail;

	private boolean isCompany;

	@Size(max = 20, message = "{bakery.phone.number.invalid}")
	// A simple (Finnish) VAT id checker
	@Pattern(regexp = "^FI(\\d){8}$", message = "{bakery.phone.number.invalid}")
	private String vatID;

	@ElementCollection
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

}
