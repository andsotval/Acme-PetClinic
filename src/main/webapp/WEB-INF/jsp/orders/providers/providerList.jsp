<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="orders/providers">
    <h2>Select a Provider</h2>

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

    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/providers.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>