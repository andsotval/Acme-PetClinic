<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{ownerId}">
    <h2>Visits</h2>

    <table id="ownersTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${visits}" var="visit">
            <tr>
                <td>
                    <c:out value="${visit.date}"/>
                </td>
                <td>
                    <c:out value="${visit.description} "/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <table class="table-buttons">
        <tr>
            <td>
            
            </td>            
        </tr>
    </table>
</petclinic:layout>