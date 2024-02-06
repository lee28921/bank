package com.tenco.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.repository.interfaces.UserRepository;

@Service // IoC 대상
public class UserService {
	
	// 생성자 의존 주입 DI
	//@Autowired
	private UserRepository repository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
				.password(passwordEncoder.encode(dto.getPassword()))
				.originFileName(dto.getOriginFileName())
				.uploadFileName(dto.getUploadFileName())
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
		
		// 사용자 username 받아서 정보를 추출
		User userEntity = repository.findByUsername(dto.getUsername());
		if(userEntity == null) {
			throw new CustomRestfulException("존재하지 않는 계정입니다", HttpStatus.BAD_REQUEST);
		}
		
		boolean isPwdMatched =
					passwordEncoder.matches(dto.getPassword(), 
							userEntity.getPassword());
		
		if(isPwdMatched == false) {
			throw new CustomRestfulException("비밀번호가 잘못되었습니다", HttpStatus.UNAUTHORIZED);
		}
		
		User user = User.builder()
				.username(dto.getUsername())
				.password(dto.getPassword())
				.build();
		
		repository.findByUsernameAndPassword(user);
		
		return userEntity;
	}
	
	// 사용자 이름만 가지고 정보 조회
	public User readUserByUserName(String username) {
		
		return repository.findByUsername(username);
	}
}
