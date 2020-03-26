<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits">
    <h2>Veterinarians</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Actions</th>
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
                <td>
                	<spring:url value="/visits/delete/{visitId}" var="visitUrl">
                        <spring:param name="visitId" value="${visit.id}"/>
                    </spring:url>
                	<a href="${fn:escapeXml(visitUrl)}">Cancel</a>
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
