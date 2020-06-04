<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="specialties">
    <h2>Specialty</h2>
    
    <div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>

    <table id="specialtyTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${specialties}" var="specialty">
            <tr>
                <td width="90%">
                	<c:out value="${specialty.name}"/>
                </td>
                <td width="10%">
                	<spring:url value="/specialty/admin/edit/{specialtyId}" var="specialtyUpdateUrl">
                        <spring:param name="specialtyId" value="${specialty.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(specialtyUpdateUrl)}"><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
                    
                    <c:choose>
	                    <c:when test="${available eq true}">
			                <spring:url value="/specialty/admin/notAvailable/{specialtyId}" var="specialtyNotAvailableUrl">
			                    <spring:param name="specialtyId" value="${specialty.id}"/>
			                </spring:url>
			                <a href="${fn:escapeXml(specialtyNotAvailableUrl)}"><span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span></a>
	                    </c:when>
	                    <c:otherwise>
	                    	 <spring:url value="/specialty/admin/available/{specialtyId}" var="specialtyAvailableUrl">
			                    <spring:param name="specialtyId" value="${specialty.id}"/>
			                </spring:url>
			                <a href="${fn:escapeXml(specialtyAvailableUrl)}"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
	                    </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <a class="btn btn-default" href='<spring:url value="/specialty/admin/new" htmlEscape="true"/>'>Create a new Specialty</a>
	<c:choose>
		<c:when test="${available eq true}">
			<a class="btn btn-default" href='<spring:url value="/specialty/admin/listNotAvailable" htmlEscape="true"/>'>Not Availables</a>
		</c:when>
		<c:otherwise>
			<a class="btn btn-default" href='<spring:url value="/specialty/admin/listAvailable" htmlEscape="true"/>'>Availables</a>
		</c:otherwise>
	</c:choose>

</petclinic:layout>