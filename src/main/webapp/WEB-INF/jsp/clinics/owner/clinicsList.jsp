<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="clinics">
    <h2>Clinics</h2>

    <table id="clinicsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>City</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${clinics}" var="clinic">
            <tr>
                <td>
                	<spring:url value="/clinics/owner/{clinicId}" var="clinicUrl">
                        <spring:param name="clinicId" value="${clinic.id}"/>
                    </spring:url>
                    <a href="${fn:escapeXml(clinicUrl)}"><c:out value="${clinic.name}"/></a>
                </td>
                <td>
                    <c:out value="${clinic.city}"/>
                </td>       
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>