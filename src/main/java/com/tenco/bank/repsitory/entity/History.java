package com.tenco.bank.repsitory.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History {
 
	private Integer id;
	private Long amount; // bigint(DB) -> Long(Java)
	private Integer wAccountId;
	private Integer dAccountId;
	private Long wBalance;
	private Long dBalance;
	private Timestamp createdAt;
}
