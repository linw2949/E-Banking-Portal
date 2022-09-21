package com.winnie.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Data
@AllArgsConstructor
public class JwtReq implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotBlank(message = "userName is not allowed to be blank")
	@Size(max = 16, message = "userName is not longer than 16 digits")
	private String userName;
	@NotBlank(message = "password is not allowed to be blank")
	@Size(max = 16, message = "password is not longer than 16 digits")
	private String password;
}