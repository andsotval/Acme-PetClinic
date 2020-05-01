<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="orders">
	<h2>Orders</h2>

	<c:if test="${empty orders}">
		<h3>You don't have orders by the moment...</h3>
	</c:if>

	<c:if test="${not empty orders}">
		<table id="ordersTable" class="table table-striped">
			<thead>
				<tr>
					<th>Date</th>
					<th>Status</th>
					<!-- <th>Products</th> -->
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${orders}" var="order">
					<tr>
						<td><spring:url value="/orders/{orderId}" var="orderUrl">
								<spring:param name="orderId" value="${order.id}" />
							</spring:url> <a href="${fn:escapeXml(orderUrl)}"><c:out
									value="${order.date}" /></a></td>
						<td><c:choose>
								<c:when test="${order.isAccepted eq false}">
									<c:out value="PENDING" />
								</c:when>
								<c:otherwise>
									<c:out value="ACCEPTED" />
								</c:otherwise>
							</c:choose></td>
						<!-- <td>
							 <c:forEach var="product" items="${productOrder}">
								<c:out value="[${product.name}]"/>
							</c:forEach> 
						</td> -->
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<a class="btn btn-default"
		href='<spring:url value="/orders/providers/listAvailable" htmlEscape="true"/>'>Create
		a new Order</a>

</petclinic:layout>