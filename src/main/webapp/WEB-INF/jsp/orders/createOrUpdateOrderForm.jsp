<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="orders">
	<h2>New Order</h2>
	<form:form class="form-horizontal"
		id="create-order-form" action="/orders/save/${providerId}">

		<table id="productsTable" class="table table-striped">
			<thead>
				<tr>
					<th width="70%">Name</th>
					<th>Price</th>
					<th>Tax % Include</th>
					<th>Amount</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${products}" var="product">
					<input type="hidden" name="productIds" value="${product.id}"/>
					<tr>
						<td><c:out value="${product.name}" /></td>
						<td align="center"><c:out value="${product.price}" /></td>
						<td align="center"><c:out value="${product.tax}" /></td>
						<td>
							<input id="input"  type="number" min="0" name="amountNumber" value="0">
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="form-group">
			<a class="btn btn-default"
				href='<spring:url value="/orders/providers/listAvailable" htmlEscape="true"/>'>Back
				to Providers List</a>
			<button class="btn btn-default" type="submit">Create Order</button>
		</div>
	</form:form>
	
</petclinic:layout>