package com.tenco.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private HttpSession httpSession;
	
	/**
	 * 회원 가입 페이지 요청
	 * @return
	 */
	// http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUpPage() {
		
		
		return "user/signUp";
	}
	
	// 회원 가입 요청 처리
	// 주소 설계 http://localhost:80/user/sign-up
	
	/**
	 * 회원 가입 요청
	 * @param dto
	 * @return 로그인 페이지(/sign-up)
	 */
	
	@PostMapping("/sign-up")
	public String signProc(SignUpFormDto dto) {
		
		// 1. 인증검사 x
		// 2. 유효성 검사
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력하세요", 
					HttpStatus.BAD_REQUEST);
			
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("password을 입력하세요", 
					HttpStatus.BAD_REQUEST);
			
		}
		if(dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new CustomRestfulException("fullname을 입력하세요", 
					HttpStatus.BAD_REQUEST);
		}
		
		service.createUser(dto);
		
		return "redirect:/user/sign-up";
	}
	
	/**
	 * 로그인 페이지 요청
	 * @param dto
	 */
	
	// 1. 로그인 페이지 요청 처리 -- page 요청
	// http://localhost:80/user/sign-in
	@GetMapping("/sign-in")
	public String signInPage() {
		
		
		return "user/signIn";
	}
	
	/**
	 * 로그인 요청 처리
	 * @param SignInFormDto
	 * @return 추후 account/list 페이지로 이동 예정(todo)
	 */
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto dto) {

		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력하시오", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("password를 입력하시오", HttpStatus.BAD_REQUEST);
		}
		
		User user = service.readUser(dto);
		httpSession.setAttribute("principal", user);
		
		return "redirect:/account/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		httpSession.invalidate();
		return "redirect:/user/sign-in";
	}
	
	
}
