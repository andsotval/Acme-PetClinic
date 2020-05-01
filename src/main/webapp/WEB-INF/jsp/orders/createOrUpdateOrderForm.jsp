<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="orders">
	<h2>New Order</h2>
	<form:form modelAttribute="products" class="form-horizontal"
		id="create-order-form">

		<input type="hidden" name="id" value="${order.id}" />
		<input type="hidden" name="manager.id" value="${order.manager.id}" />
		<input type="hidden" name="isAccepted" value="false" />
		<input type="hidden" name="date"
			value="<petclinic:localDate date="${order.date}" pattern="yyyy/MM/dd" />" />

		<table id="productsTable" class="table table-striped">
			<thead>
				<tr>
					<th>Name</th>
					<th>Price</th>
					<th>Tax %</th>
					<th>Amount</th>
					<th>Add to Order</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${products}" var="product">
					<input type="hidden" name="name" value="${product.name}" />
					<input type="hidden" name="price" value="${product.price}" />
					<input type="hidden" name="tax" value="${product.tax}" />
					<tr>
						<td><c:out value="${product.name}" /></td>
						<td><c:out value="${product.price}" /></td>
						<td><c:out value="${product.tax}" /></td>
						<td><input type="number" name="product.amount"
							value="${product.amount}"></td>
						<td><input type="checkbox" name="product.order.id"
							value="${order.id}"></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<%--  <input type="hidden" name="id" value="${order.id}" />
		<input type="hidden" name="manager.id" value="${order.manager.id}" />
		<input type="hidden" name="date"
			value="<petclinic:localDate date="${order.date}" pattern="yyyy/MM/dd" />" />
		<input type="hidden" name="isAccepted" value="false" /> -->	
		<!-- <div class="form-group has-feedback"> 
            <petclinic:selectField label="Product" name="productOrder" size="5" itemLabel="name" names="${products}"></petclinic:selectField> -->
		<!-- <form:select multiple="true" path="product" items="${products}" itemLabel="name" itemValue="id" /> -->
		<!-- </div> --%>

		<%-- <form:input path="product[${status.index}].amount" name="Amount" value="amount"/> --%>



		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<a class="btn btn-default"
					href='<spring:url value="/orders/providers/listAvailable" htmlEscape="true"/>'>Back
					to Providers List</a>
				<button class="btn btn-default" type="submit">Create Order</button>
			</div>
		</div>
	</form:form>

</petclinic:layout>