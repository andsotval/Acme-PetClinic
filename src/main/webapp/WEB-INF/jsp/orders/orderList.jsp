<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="orders">
    <h2>Orders</h2>

    <table id="ordersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Is Accepted</th>
            <th>Products</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${orders}" var="order">
            <tr>
                <td>
                	<spring:url value="/orders/{orderId}" var="orderUrl">
                        <spring:param name="orderId" value="${order.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(orderUrl)}"><c:out value="${order.date}"/></a>
                </td>
                <td>
                    <c:out value="${order.isAccepted}"/>
                </td>
                <td>
                    <c:forEach var="product" items="${order.product}">
                        <c:out value="[${product.name} - ${product.price}] "/>
                    </c:forEach>
                </td>             
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <a class="btn btn-default" href='<spring:url value="/orders/providers/listAvailable" htmlEscape="true"/>'>Create a new Order</a>
    
    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/providers.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>