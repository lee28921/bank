package com.tenco.bank.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tenco.bank.dto.KakaoProfile;
import com.tenco.bank.dto.OauthToken;
import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.UserService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user")
@PropertySource("classpath:application.yml")
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
		
		System.out.println("dto : "+dto.toString());
		System.out.println(dto.getFile().getOriginalFilename());
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
		
		// 파일 업로드
		MultipartFile file = dto.getFile();
		if(file.isEmpty() == false) {
			// 사용자가 이미지를 업로드 했다면 기능 구현
			
			// 파일 사이즈 체크
			// 20MB
			if(file.getSize() > Define.MAX_FILE_SIZE) {
				throw new CustomRestfulException("파일 크기는 20MB 이상 클 수는 없습니다", HttpStatus.BAD_REQUEST);
			}
			
			// 서버 컴퓨터에 파일 넣을 디렉토리가 있는 지 검사
			String saveDirectory = Define.UPLOAD_FILEDERECTORY;
			// 폴더가 없다면 오류 발생(파일 생성시)
			File dir = new File(saveDirectory);
			if(dir.exists() == false) {
				dir.mkdir(); // 폴더가 없으면 폴더 생성
			}
			
			// 파일 이름 (중복 처리 예방)
			UUID uuid = UUID.randomUUID();
			String fileName = uuid + "_" + file.getOriginalFilename();
			System.out.println("fileName : "+fileName);
			
			// C:\\wok_spring\\upload\(파일 이름).png
			String uploadtPath = Define.UPLOAD_FILEDERECTORY + File.separator + fileName;
			File destination = new File(uploadtPath);
			
			try {
				file.transferTo(destination);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			
			// 객체 상태 변경
			dto.setOriginFileName(file.getOriginalFilename());
			dto.setUploadFileName(fileName);
			
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
		log.info("KakaoRestKey : "+KakaoRestKey+", KakaoRedirectUri : "+KakaoRedirectUri);
		
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
	
	// 로그아웃 기능 만들기
	@GetMapping("/logout")
	public String logout() {
		httpSession.invalidate();
		return "redirect:/user/sign-in";
	}
	
	// Rest Key
	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String KakaoRestKey;
	
	// 리다이렉트 uri
	@Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
	private String KakaoRedirectUri;
	
	// http://lcoalhost:80/user/kakao-callback?code="{카카오 코드}"
	@GetMapping("/kakao-callback")
	public String kakaoCallback(@RequestParam String code) {
		// POST 방식, Header 구성, body 구성
		RestTemplate rt1 = new RestTemplate();
		// 헤더 구성
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// body 구성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", KakaoRestKey);
		params.add("redirect_uri", KakaoRedirectUri);
		params.add("code", code);
		
		// 헤더 + 바디 결합
		HttpEntity<MultiValueMap<String, String>> reqMsg
			= new HttpEntity<>(params, headers1);
		
		ResponseEntity<OauthToken> response = rt1.exchange("https://kauth.kakao.com/oauth/token", 
				HttpMethod.POST, reqMsg, OauthToken.class);
		
		// 다시 요청 -- 인증 토큰 -- 사용자 정보 요청
		RestTemplate rt2 = new RestTemplate();
		
		// 헤더
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer "+ response.getBody().getAccessToken());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 바디 x
		// 결합 --> 요청
		HttpEntity<MultiValueMap<String, String>> kakaoInfo 
				= new HttpEntity<>(headers2);
		ResponseEntity<KakaoProfile> response2 = 
				rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoInfo, KakaoProfile.class);
		
		System.out.println(response2.getBody());
		
		KakaoProfile kakaoProfile = response2.getBody();
		
		// 단 최소 요청 사용자라 -> 회원 후 로그인 처리
		// 우리 사이트 --> 카카오
		SignUpFormDto dto = SignUpFormDto.builder()
				.username("OAuth_"+ kakaoProfile.getProperties().getNickname())
				.fullname("Kakao")
				.password("asd1234")
				.build();
		
		// oldUser 객체 선언(안하면 null로 받게 됨)
		User oldUser = new User();
		
		oldUser = service.readUserByUserName(dto.getUsername()); // 조회
		if(oldUser == null) {
			service.createUser(dto);
			///////////////////////////
			oldUser.setUsername(dto.getUsername());
			oldUser.setFullname(dto.getFullname());
		}
		oldUser.setPassword(null);
		// 로그인 처리
		httpSession.setAttribute(Define.PRINCIPAL, oldUser);
		
		return "redirect:/account/list";
	}
}
