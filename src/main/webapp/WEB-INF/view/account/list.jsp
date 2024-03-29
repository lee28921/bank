<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp" %>

		<div class="col-sm-8">
			<h2>나의 계좌 목록</h2>
			<h5>어서오세요 환영합니다</h5>
			
			<c:choose>
				<c:when test="${accountList != null}">
					<table class="table table-hover">
						<thead>
							<tr>
								<td>계좌 번호</td>
								<td>잔액</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="account" items="${accountList}">
							<tr>
								<td><a href="/account/detail/${account.id}">${account.number}</a></td>
								<td>${account.formatBalance()}</td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<p>아직 생성된 계좌가 없습니다</p>
				</c:otherwise>
			</c:choose>
			
		</div>
	</div>
</div>

<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp" %>