<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<petclinic:layout pageName="orders">

	<h2>Order Information</h2>


	<table class="table table-striped">
		<tr>
			<th>Date</th>
			<td><petclinic:localDate date="${order.date}"
					pattern="yyyy-MM-dd" /></td>
		</tr>
		<tr>
			<th>Status</th>
			<td><c:choose>
					<c:when test="${order.isAccepted eq false}">
						<c:out value="REJECTED" />
					</c:when>
					<c:when test="${order.isAccepted eq true}">
						<c:out value="ACCEPTED" />
					</c:when>
					<c:otherwise>
						<c:out value="PENDING" />
					</c:otherwise>
				</c:choose></td>
		</tr>
	</table>

	<c:set var = "total" value = "${0}"/>
	<h2>Products</h2>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>Products</th>
				<th>Unit price</th>
				<th>Amount</th>
				<th>Total price</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="productsOrder" items="${productsOrder}">
				<tr>
					<td><c:out value="${productsOrder.product.name}" /></td>
					<td><c:out value="${productsOrder.price}" /> &#8364</td>
					<td><c:out value="${productsOrder.amount}" /></td>
					<td><fmt:formatNumber type = "number" maxFractionDigits = "2" 
						value = "${productsOrder.price * productsOrder.amount}" /> &#8364</td>
				</tr>
				<c:set var = "total" value = "${total + (productsOrder.price * productsOrder.amount)}"/>
			</c:forEach>
		</tbody>
		<tfoot>
			<tr>
				<td><strong><c:out value="TOTAL" /></strong></td>
				<td colspan="2"></td>
				<td><strong><fmt:formatNumber type = "number" maxFractionDigits = "2" 
						value = "${total}" /> &#8364</strong></td>
			</tr>
		</tfoot>
	</table>

	<h2>Provider</h2>
	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<th>Telephone</th>
			<th>Mail</th>
			<tr>
				<td><c:out value="${provider.firstName} ${provider.lastName}" /></td>
				<td><c:out value="${provider.telephone}" /></td>
				<td><c:out value="${provider.mail}" /></td>
			</tr>
		</tr>
	</table>

	<a class="btn btn-default"
		href='<spring:url value="/orders/list" htmlEscape="true"/>'>Back
		to Order list</a>

</petclinic:layout>
