package com.tenco.bank.repository.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
	
	private Integer id;
	private String number;
	private String password;
	private Long balance;
	private Integer userId;
	private Timestamp createdAt;
	
	// 출금 기능
	public void withdraw(Long amount) { // 메서드 시그니쳐
		// 메서드 바디
		// 방어적 코드 작성 - todo 
		// (마이너스 금액 및 출금할 금액이 잔액보다 높으면 안됨)
		this.balance -= amount;
		
	}
	
	// 입금 기능
	public void deposit(Long amount) {
		this.balance += amount;
	}
	
	
	// 패스워드 체크
	// 잔액 여부 확인 기능
	// 계좌 소유자 확인 기능
}
