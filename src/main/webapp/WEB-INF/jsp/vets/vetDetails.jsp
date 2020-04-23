<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="vets">

	<h2>Veterinarian Information</h2>


	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out value="${vet.firstName} ${vet.lastName}" /></b></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${vet.address}" /></td>
		</tr>
		<tr>
			<th>City</th>
			<td><c:out value="${vet.city}" /></td>
		</tr>
		<tr>
			<th>Telephone</th>
			<td><c:out value="${vet.telephone}" /></td>
		</tr>
		<tr>
			<th>Specialties</th>
			<td>
				<c:forEach var="specialty" items="${vet.specialties}">				
						<c:out value="[${specialty.name}] " />				
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th>Clinic</th>
			<c:choose>
				<c:when test="${vet.clinic != null}">
					<td><c:out value="${vet.clinic.name} - ${vet.clinic.address} (${vet.clinic.city})" /></td>
				</c:when>
				<c:otherwise>
					<td>the vet is not yet hired at any clinic</td>
				</c:otherwise>
			</c:choose>
		</tr>
	</table>

	<a class="btn btn-default"
		href='<spring:url value="/vets/vetsAvailable" htmlEscape="true"/>'>Back to list of veterinarian</a>

</petclinic:layout>