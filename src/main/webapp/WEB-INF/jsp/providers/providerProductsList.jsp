<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="/providers">
	<h2><c:out value="Provider: ${provider.firstName} ${provider.lastName}"/></h2>

	<c:if test="${empty products}">
		<h3>The provider doesn't have products available...</h3>
	</c:if>
	
	<c:if test="${not empty products}">
    <table id="productsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Price</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product">
            <tr>
                <td>
                    <c:out value="${product.name}"/>
                </td>
                <td>
                    <c:out value="${product.price}"/> &#8364
                </td>           
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>
    
   	<a class="btn btn-default"
	href='<spring:url value="/providers/listAvailable" htmlEscape="true"/>'>Back to list of Providers</a>
   
</petclinic:layout>