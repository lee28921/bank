package com.tenco.bank.repsitory.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	// 기본 생성자
	
	private Integer id;
	private String username;
	private String password;
	private String fullname;
	private Timestamp createdAt;
}
