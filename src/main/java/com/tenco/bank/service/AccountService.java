package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.WithDrawFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.History;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.utils.Define;

import lombok.Builder;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service // IoC 대상 + 싱글톤으로 관리 됨
public class AccountService {
	
	// OCP - 
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private HistoryRepository historyRepository;
	
	// 계좌 생성
	@Transactional
	public void createAccount(AccountSaveFormDto dto, Integer principalId) {
		
		// 계좌 번호 중복 확인
		if(readAccount(dto.getNumber()) != null) {
			throw new CustomRestfulException(Define.EXIST_ACCOUNT, HttpStatus.BAD_REQUEST);
		}
		
		// 예외 처리
		
		Account account = new Account();
		account.setNumber(dto.getNumber());
		account.setPassword(dto.getPassword());
		account.setBalance(dto.getBalance());
		account.setUserId(principalId);
		
		int resultRowCount = repository.insert(account);
		
		if(resultRowCount != 1) {
			throw new CustomRestfulException(Define.FAIL_TO_CREATE_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	// 단일 계좌 검색 기능
	public Account readAccount(String number) {
		return repository.findByNumber(number.trim());
	}
	
	// 계좌 목록 보기 기능
	public List<Account> readAccountListByUserId(Integer principalId) {
		
		return repository.findAllByUserId(principalId);
	}
	
	// 출금 기능 만들기
	// 1. 계좌 존재 여부 확인 -- select
	// 2. 본인 계좌 여부 확인 -- select
	// 3. 계좌 비번 확인
	// 4. 잔액 여부 확인
	// 5. 출금 처리 ---> update
	// 6. 거래 내역 등록 --> insert
	// 7. 트랜잭션 처리
	@Transactional
	public void updateAccountWithdraw(WithDrawFormDto dto, Integer principalId) {
		// 1
		Account accountEntity = repository.findByNumber(dto.getWAccountNumber());
		
		if(accountEntity == null) {
			throw new CustomRestfulException(Define.NOT_EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// 2
		accountEntity.checkOwner(principalId);
		
		// 3 (String) 불변
		accountEntity.checkPassword(dto.getWAccountPassword());
		
		// 4
		accountEntity.checkBalance(dto.getAmount());
		
		// 5 --> 출금 기능 --> 객체 상태값 변경
		accountEntity.withdraw(dto.getAmount());
		repository.updateById(accountEntity);
		
		// 6
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(accountEntity.getBalance());
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getUserId());
		history.setDAccountId(null);
		
		int rowResultCount = historyRepository.insert(history);
		if(rowResultCount != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	// 입금 등록
	@Transactional
	public void updateAccountDeposit(DepositFormDto dto, Integer principalId) {
		// 1. 계좌 확인
		Account accountEntity = repository.findByNumber(dto.getDAccountNumber());
		if(accountEntity == null) {
			throw new CustomRestfulException("계좌를 확인할 수 없습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		// 2. 사용자 확인
		accountEntity.checkOwner(principalId);
		
		// 3. 계좌 비밀번호 확인
		accountEntity.checkPassword(dto.getDAccountPassword());
		
		// 4. 입금 처리
		accountEntity.deposit(dto.getAmount());
		repository.updateById(accountEntity);
		
		// 5. 거래 내역 등록
		History history = History.builder()
							.amount(dto.getAmount())
							.dAccountId(principalId)
							.wAccountId(null)
							.dBalance(accountEntity.getBalance())
							.wBalance(null)
							.build();
		
		int rowResultCount = historyRepository.insert(history);
		if(rowResultCount != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
