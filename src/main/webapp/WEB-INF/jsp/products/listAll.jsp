<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="/providers">
	<h2><c:out value="Provider: ${provider.firstName} ${provider.lastName}"/></h2>


    <table id="productsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Tax</th>
            <th>State</th>
            <th>Enable/Disable</th>
            <th>Edit</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product">
            <tr>
                <td width="30%">
                    <c:out value="${product.name}"/>
                </td>
                <td width="20%">
                    <c:out value="${product.price}"/> &#8364
                </td>           
                <td width="20%">
                    <c:out value="${product.tax}"/>
                </td>           
                <td width="20%">
                	<c:if test="${product.available}">
                	    <c:out value="Available"/> 
                    </c:if>
                	<c:if test="${!product.available}">
                	    <c:out value="Not available"/> 
                    </c:if>
                </td>  
                 
                    
                <td width="5%" style="text-align: center;">
                	<spring:url value="/product/desactivateProduct/{productId}" var="activateOrDesactivateProduct">
	                    <spring:param name="productId" value="${product.id}"/>
	                </spring:url>
	                <c:if test="${product.available}">
	                	<a href="${fn:escapeXml(activateOrDesactivateProduct)}"><span class="glyphicon glyphicon-ok" style=" color: #6db33f" aria-hidden="true"></span></a>
                	</c:if>
	                <c:if test="${!product.available}">
	                	<a href="${fn:escapeXml(activateOrDesactivateProduct)}"><span class="glyphicon glyphicon-remove" style="color: #FF0000" aria-hidden="true"></span></a>
                	</c:if>
                </td>     
                <td width="5%" style="text-align: center;">
                	<spring:url value="/product/show/{productId}" var="showProduct">
	                    <spring:param name="productId" value="${product.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(showProduct)}"><span class="glyphicon glyphicon-edit" style=" color: #6db33f" aria-hidden="true"></span></a>
                </td>           
            </tr>
            
        </c:forEach>
        </tbody>
    </table>
    
   	<a class="btn btn-default"
	href='<spring:url value="/providers/listAvailable" htmlEscape="true"/>'>Back to list of Providers</a>
   
   	<a class="btn btn-default"
	href='<spring:url value="/product/initNewProduct" htmlEscape="true"/>'>Create Product</a>
   
</petclinic:layout>