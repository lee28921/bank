package com.tenco.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.utils.Define;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//1. HandlerInterceptor 구현하기
//AuthInterceptor -> new  ->
@Component    // IoC 대상

public class AuthInterceptor implements HandlerInterceptor {

 // preHandle
 // 컨트롤러 들어오기 전에 동작
 // true -> 컨트롤러 안으로 들어감
 // false -> 안 들어감
 @Override
 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
     throws Exception {
	 System.out.println("인터셉터 동작 확인 ----------------");
	 // 인증 정보
	 HttpSession session = request.getSession();
	 User principal = (User) session.getAttribute(Define.PRINCIPAL);
	 
	 if(principal == null) {
		 
		 //response.sendRedirect("/user/sign-in");
		 // 예외 처리
		 throw new UnAuthorizedException("로그인 먼저 해주세요.", HttpStatus.UNAUTHORIZED);
	 }
	 
     return HandlerInterceptor.super.preHandle(request, response, handler);
 }

 // postHandle
 //뷰가 렌더링 되기 전에 호출되는 메서드
 @Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

 @Override
 public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
         @Nullable Exception ex) throws Exception {
	 
 }
}