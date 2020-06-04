<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="orders">
	<h2>My Orders</h2>
	
	<div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>

	<c:if test="${empty orders}">
		<h3>You don't have orders by the moment...</h3>
	</c:if>

	<c:if test="${not empty orders}">
		<table id="ordersTable" class="table table-striped">
			<thead>
				<tr>
					<th>Date</th>
					<th>Manager</th>
					<th>Status</th>
					<th style="text-align: center;">Action</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${orders}" var="order">
					<tr>
						<td><spring:url value="/orders/provider/{orderId}" var="orderUrl">
								<spring:param name="orderId" value="${order.id}" />
							</spring:url> <a href="${fn:escapeXml(orderUrl)}"><c:out
									value="${order.date}" /></a></td>
						<td>
							<c:out value="${order.manager.firstName}"/>
							<c:out value=" "/>
							<c:out value="${order.manager.lastName}"/>
						</td>
						<td>
							<c:choose>
								<c:when test="${order.isAccepted eq false}">
									<c:out value ="REJECTED"/>
								</c:when>
								<c:when test="${order.isAccepted eq true}">
									<c:out value ="ACCEPTED"/>
								</c:when>
								<c:otherwise>
									<c:out value ="PENDING"/>
								</c:otherwise>
							</c:choose>
							
						</td>
						<td style="text-align: center;">
							<c:choose>
								<c:when test="${order.isAccepted eq null}">
									<spring:url value="/orders/acceptOrder/{orderId}" var="acceptOrder">
			                  	  		<spring:param name="orderId" value="${order.id}"/>
			                		</spring:url>
	                				<a href="${fn:escapeXml(acceptOrder)}">
	                					<span class="glyphicon glyphicon-ok" style="color: #6db33f" aria-hidden="true"></span>
	                				</a>
	                				
	                				<spring:url value="/orders/rejectedOrder/{orderId}" var="rejectedOrder">
			                  	  		<spring:param name="orderId" value="${order.id}"/>
			                		</spring:url>
	                				<a href="${fn:escapeXml(rejectedOrder)}">
	                					<span class="glyphicon glyphicon-remove" style="color: #FF0000" aria-hidden="true"></span>
	                				</a>
								</c:when>
								<c:otherwise>
									<c:out value ="-"/>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

</petclinic:layout>