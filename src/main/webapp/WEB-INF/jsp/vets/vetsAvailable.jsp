<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="vets">
	<h1>Veterinarians</h1>
	
	<div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>

	<h2>Available veterinarians for hire</h2>
	<c:if test="${not empty vets2}">
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
							<spring:url value="/vets/{vetId}" var="vetUrl">
								<spring:param name="vetId" value="${vet.id}" />
							</spring:url>
							<a href="${fn:escapeXml(vetUrl)}">
								<c:out value="${vet.firstName} ${vet.lastName}" />
							</a>
						</td>
						<td><c:forEach var="specialty" items="${vet.specialties}">
								<c:out value="[${specialty.name}] " />
							</c:forEach> <c:if test="${vet.nrOfSpecialties == 0}">none</c:if></td>
						<td><c:if test="${vet.clinic == null}">
								<spring:url value="/vets/accept/{vetId}" var="vetUrlAccept">
									<spring:param name="vetId" value="${vet.id}" />
								</spring:url>
								<a href="${fn:escapeXml(vetUrlAccept)}"><span
									class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
							</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<c:if test="${empty vets2}">
		<h3>You don't have any veterinarians available at the moment to hire...</h3>
	</c:if>

	<h2>Veterinarians hired by your clinic</h2>
	<c:if test="${not empty hiredVets}">
		<table id="vetsOfClinicTable" class="table table-striped">
			<thead>
				<tr>
					<th>Name</th>
					<th>Specialties</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${hiredVets}" var="vet">
					<tr>
						<td>
							<spring:url value="/vets/{vetId}" var="vetUrl">
								<spring:param name="vetId" value="${vet.id}" />
							</spring:url>
							<a href="${fn:escapeXml(vetUrl)}">
								<c:out value="${vet.firstName} ${vet.lastName}" />
							</a>
						</td>
						<td>
							<c:forEach var="specialty" items="${vet.specialties}">
								<c:out value="[${specialty.name}] " />
							</c:forEach> <c:if test="${vet.nrOfSpecialties == 0}">none</c:if></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<c:if test="${empty hiredVets}">
		<h3>You don't have any veterinarians hired at the moment in your clinic</h3>
	</c:if>
</petclinic:layout>