<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

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
			<td>
				<c:choose>
					<c:when test="${order.isAccepted eq false}">
						<c:out value="PENDING" />
					</c:when>
					<c:otherwise>
						<c:out value="ACCEPTED" />
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</table>
	
	<h2>Products</h2>
	<table class="table table-striped">
		<tr>
			<th>Products</th>
			<c:forEach var="product" items="${order.product}">
				<tr>
					<td><c:out value="${product.name}" /></td>
					<td><c:out value="${product.price}" /></td>
				</tr>
			</c:forEach>
		</tr>
	</table>
	
	<h2>Provider</h2>
	<table class="table table-striped">
		<tr>
			<th>Provider</th>
			<td><c:out value="${orderProvider.firstName} ${orderProvider.lastName}"/></td>
			<td><c:out value="${orderProvider.telephone}" /></td>
			<td><c:out value="${orderProvider.mail}" /></td>
			
		</tr>
	</table>
	
	<a class="btn btn-default" href='<spring:url value="/orders/list" htmlEscape="true"/>'>Back to Order list</a>

</petclinic:layout>
