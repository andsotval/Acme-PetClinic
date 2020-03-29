<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="orders">
    <h2>Veterinarians</h2>

    <table id="ordersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Accepted?</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${orders.orderList}" var="order">
            <tr>
                <td>
                    <c:out value="${order.date}"/>
                </td>
                <td>
                    <c:out value="${order.isAccepted}"/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
            <td>
                <a href="<spring:url value="/vets.xml" htmlEscape="true" />">View as XML</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>
