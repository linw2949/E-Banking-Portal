package com.winnie.demo.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "account")
public class DAOAccount{
	@Id
	@Column
	@NotBlank
	@Size(max = 50)
	private String iban;
	@Column(updatable = false, nullable = false)
	@NotBlank
	@Size(max = 16)
	private String userId;
}