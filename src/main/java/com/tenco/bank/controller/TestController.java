package com.tenco.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.handler.exception.CustomPageException;
import com.tenco.bank.handler.exception.CustomRestfulException;

@Controller
@RequestMapping("/test1") // 대문
public class TestController {
	
	// 주소 설계
	// http://localhost:80/test1/main
	@GetMapping("/main")
	public void mainPage() {
		System.out.println("hello bank!");
		// 인증 검사
		// 유효성 검사
		// 뷰 리졸브 --> 해당하는 파일을 찾아(data)
		// 전체 경로 : /WEB-INF/view/layout/main
		// prefix : /WEB-INF/view
		// suffix : .jsp
		
		// 예외 발생
		throw new CustomPageException("페이지가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
		
		//return "/layout/main";
	}
}
