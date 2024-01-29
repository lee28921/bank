package com.tenco.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.repository.interfaces.UserRepository;

@Service // IoC 대상
public class UserService {
	
	// 생성자 의존 주입 DI
	@Autowired
	private UserRepository repository;
	
	// Autowired 역할
	public UserService(UserRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * 회원 가입 로직 처리
	 * @param SignUpFormDto
	 * return void
	 */
	
	// 회원가입
	@Transactional
	public void createUser(SignUpFormDto dto) {
		User user = User.builder()
				.username(dto.getUsername())
				.password(dto.getPassword())
				.fullname(dto.getFullname())
				.build();
		
		// 사용자 
		int result = repository.insert(user);
		
		if(result != 1) {
			throw new CustomRestfulException("회원 가입 실패", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 로그인
	public User readUser(SignInFormDto dto) {
		
		User user = User.builder()
				.username(dto.getUsername())
				.password(dto.getPassword())
				.build();
		
		User userEntity = repository.findByUsernameAndPassword(user);
		
		if(userEntity == null) {
			throw new UnAuthorizedException("인증된 사용자가 아닙니다",HttpStatus.UNAUTHORIZED);
		}
		
		
		return userEntity;
	}
}