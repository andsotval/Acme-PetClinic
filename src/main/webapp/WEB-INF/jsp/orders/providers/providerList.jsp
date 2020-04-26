<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="orders/providers">
    <h2>Select a Provider</h2>

	<c:if test="${empty providers}">
		<h3>You don't have providers by the moment...</h3>
	</c:if>

	<c:if test="${not empty providers}">
    <table id="providersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Mail</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${providers}" var="provider">
            <tr>
                <td>
                    <c:out value="${provider.firstName} ${provider.lastName}"/>
                </td>
                <td>
                    <c:out value="${provider.mail}"/>
                </td>
                <td>
                    <spring:url value="/orders/new/{providerId}" var="orderUrl">                  
                    	<spring:param name="providerId" value="${provider.id}"/>                      
                    </spring:url>
                    <a href="${fn:escapeXml(orderUrl)}"><span class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span></a>
                </td>                
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a class="btn btn-default" href='<spring:url value="/orders/list" htmlEscape="true"/>'>Back to Orders List</a>
	</c:if>
</petclinic:layout>