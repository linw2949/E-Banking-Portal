package com.winnie.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DAOUser implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	@NotBlank
	@Size(max = 16)
	private String userId;
	@Column(updatable = false, nullable = false)
	@NotBlank
	@Size(max = 100)
	private String password;
}