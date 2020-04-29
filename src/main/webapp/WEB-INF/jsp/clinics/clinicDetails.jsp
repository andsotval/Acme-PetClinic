<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<petclinic:layout pageName="clinics">

	<h2>Clinic Information</h2>


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
			<td><c:out value="${clinic.manager.firstName} ${clinic.manager.lastName}" /></td>
		</tr>
	</table>
	<sec:authorize access="hasAuthority('owner')">
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
				<c:choose>
					<c:when test="${!notUnsubscribe}">
						<div class="text-left">
							<a class="btn btn-default"
								href='<spring:url value="/clinics/owner/unsubscribeFromClinic" htmlEscape="true">
					<spring:param name="clinicId" value="${clinic.id}"/> </spring:url>'>
								Unsubscribe to Clinic</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="text-right">
							<p>You cannot unsubscribe to clinic while you have <a href="/visits/listByOwner">active visits</a> or <a href="/stays/listByOwner">active stays</a></p>
						</div>
					</c:otherwise>
				</c:choose>
			</c:otherwise>
		</c:choose>
	</sec:authorize>

	<sec:authorize access="hasAuthority('veterinarian')">
		<h1>User List</h1>
		<table id="ownersTable" class="table table-striped">
			<thead>
				<tr>
					<th style="width: 150px;">Name</th>
					<th style="width: 200px;">Address</th>
					<th>City</th>
					<th style="width: 120px">Telephone</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${owners}" var="owner">
					<tr>
						<td><c:out value="${owner.firstName} ${owner.lastName}" /></td>
						<td><c:out value="${owner.address}" /></td>
						<td><c:out value="${owner.city}" /></td>
						<td><c:out value="${owner.telephone}" /></td>

					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<script>
		$(document).ready( function () {
		    $('#ownersTable').DataTable();
		} );
		</script>
	</sec:authorize>
	
</petclinic:layout>
