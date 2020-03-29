<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2>Vets</h2>
 
    <table id="vetsAvailableTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Specialties</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vets2}" var="vet">
            <tr>
                <td>
                    <c:out value="${vet.firstName} ${vet.lastName}"/>
                </td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}">
                        <c:out value="${specialty.name} "/>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
                </td>
                <td>
                	<c:if test="${vet.clinic == null}">
	                	<spring:url value="/vet/accept/{vetId}" var="vetUrlAccept">
	                        <spring:param name="vetId" value="${vet.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(vetUrlAccept)}"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true" ></span></a>
	                </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>