package com.winnie.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class JwtRes implements Serializable {
	private static final long serialVersionUID = 1L;
	private final String jwttoken;
}