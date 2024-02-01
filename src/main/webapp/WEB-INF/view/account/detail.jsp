<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp" %>

		<div class="col-sm-8 detail">
			<h2>계좌 상세 보기</h2>
			<h5>어서오세요 환영합니다</h5>
			<div class="detail--bg">
				<h2>${principal.username}님의 계좌</h2>
				<table class="table">
					<thead class="thead-dark">
						<tr>
							<td>계좌 번호</td>
							<td>현재 잔액</td>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>${account.number}</td>
							<td>${account.balance}</td>
						</tr>
					</tbody>
				</table>
				<div class="sort">
					<a class="sort-btn" href="/account/detail/${account.id}">전체 조회</a>
					<a class="sort-btn" href="/account/detail/${account.id}?type=deposit">입금 조회</a>
					<a class="sort-btn" href="/account/detail/${account.id}?type=withdraw">출금 조회</a>
				</div>
				<table class="table table-hover history--log">
					<thead>
						<tr>
							<td>날짜</td>
							<td>보낸이</td>
							<td>받은이</td>
							<td>입출금 금액</td>
							<td>계좌 잔액</td>
						</tr>
					</thead>
					<tbody>
					<c:forEach var="history" items="${historyList}">
						<tr>
							<td>${history.formatCreatedAt()}</td>
							<td>${history.sender}</td>
							<td>${history.receiver}</td>
							<td>${history.amount}</td>
							<td>${history.formatBalance()}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				<p>+ 전체보기</p>
			</div>
		</div>
	</div>
</div>

<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp" %>