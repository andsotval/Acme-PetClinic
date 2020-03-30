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
			<th>Is Accepted?</th>
			<td><c:out value="${order.isAccepted}" /></td>
		</tr>
		<tr>
			<th>Products</th>
			<c:forEach var="product" items="${order.product}">
				<tr>
					<td><c:out value="${product.name}" /></td>
					<td><c:out value="${product.price}" /></td>
				</tr>
			</c:forEach>
		</tr>
		<tr>
			<th>Provider</th>
			<td><c:out value="${orderProvider.firstName} ${orderProvider.lastName}"/></td>
			<td><c:out value="${orderProvider.telephone}" /></td>
			<td><c:out value="${orderProvider.mail}" /></td>
			
		</tr>
	</table>
	
	<a class="btn btn-default" href='<spring:url value="/orders/list" htmlEscape="true"/>'>Back to Order list</a>

</petclinic:layout>
