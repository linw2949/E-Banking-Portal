package com.winnie.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class JwtReq implements Serializable {
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
}