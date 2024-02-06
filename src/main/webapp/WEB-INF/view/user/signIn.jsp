<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp" %>

		<div class="col-sm-8">
			<h2>로그인</h2>
			<h5>어서오세요 환영합니다</h5>
			
			<form action="/user/sign-in" method="post">
			  <div class="form-group">
			    <label for="username">username:</label>
			    <input type="text" name="username" class="form-control" placeholder="Enter username" id="username" value="길동">
			  </div>
			  <div class="form-group">
			    <label for="pwd">password:</label>
			    <input type="password" name="password" class="form-control" placeholder="Enter password" id="pwd" value="1234">
			  </div>
			  <button type="submit" class="btn btn-primary">Submit</button>
			  <a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=34d3c25ce2692ecbdb09d440d42ce98e&redirect_uri=http://localhost/user/kakao-callback">
			  	<img alt="카카오" src="/images/kakao_login_medium.png"/>
			  </a>
			  <a href="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=O8k7j3VnfRljJEAx0lXP&state=STATE_STRING&redirect_uri=http://localhost/user/naver-callback">
			  	<img class="btn--naver" alt="네이버" src="/images/btnG_naver.png"/>
			  </a>
			</form>
			
		</div>
	</div>
</div>

<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp" %>