package com.tenco.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.dto.WithDrawFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private AccountService accountService;
	
	// 페이지 요청
	// http://localhost:80/account/save
	
	/**
	 * 계좌 생성 페이지 요청
	 * @return saveForm.jsp
	 */
	@GetMapping("/save")
	public String savePage() {
		// 인증 검사
		User principal = (User)session.getAttribute("principal");
		
		if(principal == null) {
			throw new UnAuthorizedException(Define.ENTER_YOUR_LOGIN, HttpStatus.UNAUTHORIZED);
			
		}
		
		return "account/saveForm";
	}
	
	/**
	 * 계좌 생성 처리
	 * @param dto
	 * @return list.jsp
	 */
	@PostMapping("/save") // body --> String --> 파싱(dto)
	public String saveProc(AccountSaveFormDto dto) {

		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException(Define.ENTER_YOUR_LOGIN, HttpStatus.UNAUTHORIZED);
			
		}
		
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfulException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		}
		if(dto.getBalance() == null || dto.getBalance() < 0) {
			throw new CustomRestfulException("잘못된 금액 입니다", HttpStatus.BAD_REQUEST);
		}
		
		accountService.createAccount(dto, principal.getId());
		
		return "redirect:/account/list";
	}
	
	/**
	 * 계좌 목록 페이지
	 * @param model - accountList
	 * @return list.jsp
	 */
	@GetMapping({"/","/list"})
	public String listPage(Model model) {
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException(Define.ENTER_YOUR_LOGIN, HttpStatus.UNAUTHORIZED);
			
		}
		
		List<Account> accountList = 
				accountService.readAccountListByUserId(principal.getId());
		
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}
		
		return "account/list";
	}
	
	// 출금 페이지 요청
	@GetMapping("/withdraw")
	public String withdrawPage() {
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException(Define.ENTER_YOUR_LOGIN, HttpStatus.UNAUTHORIZED);
			
		}
		
		return "account/withdraw";
	}
	
	// 출금 요청 로직 만들기
	@PostMapping("/withdraw")
	public String withdrawProc(WithDrawFormDto dto) {
		// 1. 인증 검사
		User principal = (User)session.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException(Define.ENTER_YOUR_LOGIN, HttpStatus.UNAUTHORIZED);
			
		}
		
		// 유효성 검사
		if(dto.getAmount() == null) {
			throw new CustomRestfulException(Define.ENTER_YOUR_ACCOUNT, 
					HttpStatus.BAD_REQUEST);
		}
		// <= 0
		if(dto.getAmount().longValue() < 0) {
			throw new CustomRestfulException("0 이상", 
					HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null) {
			throw new CustomRestfulException("계좌 번호를 입력하세요", 
					HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountPassword() == null) {
			throw new CustomRestfulException("계좌 비밀번호를 입력하세요", 
					HttpStatus.BAD_REQUEST);
		}
		
		// 서비스 호출
		accountService.updateAccountWithdraw(dto, principal.getId());
		
		return "redirect:/account/list";
	}
	
}
