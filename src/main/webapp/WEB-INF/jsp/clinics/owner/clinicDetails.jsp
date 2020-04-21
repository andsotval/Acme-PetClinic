<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="clinics">

	<h2>
		<c:choose>
			<c:when test="${owner.clinic != clinic}"> Clinic Information </c:when>
			<c:otherwise> Your Clinic Information </c:otherwise>
		</c:choose>
	</h2>


	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><c:out value="${clinic.name}" /></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${clinic.address}" /></td>
		</tr>
		<tr>
			<th>City</th>
			<td><c:out value="${clinic.city}" /></td>
		</tr>
		<tr>
			<th>Telephone</th>
			<td><c:out value="${clinic.telephone}" /></td>
		</tr>
		<tr>
			<th>Manager</th>
			<td><c:out
					value="${clinic.manager.firstName} ${clinic.manager.lastName}" /></td>
		</tr>
	</table>
	<c:choose>
		<c:when test="${owner.clinic != clinic}">
			<div class="text-left">
				<a class="btn btn-default"
					href='<spring:url value="/clinics/owner/subscribeToClinic/{clinicId}" htmlEscape="true">
					<spring:param name="clinicId" value="${clinic.id}"/> </spring:url>'>
					Subscribe to Clinic</a>
			</div>
		</c:when>
		<c:otherwise>
			<div class="text-right">
				<a class="btn btn-default"
					href='<spring:url value="/clinics/owner/unsubscribeFromClinic" htmlEscape="true"/>'>
					Unsubscribe from Clinic </a>
			</div>
		</c:otherwise>
	</c:choose>
</petclinic:layout>
