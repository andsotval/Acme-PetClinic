<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<petclinic:layout pageName="suggestion">
	<h2>Specialty list</h2>

	<div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>

	<c:if test="${empty mySpecialties}">
		<h3>You don't have specialties by the moment...</h3>
	</c:if>

	<c:if test="${not empty mySpecialties}">
		<h3>My Specialties</h3>
		<table id="mySpecialtiesTable" class="table">
			<thead>
				<tr>
					<th>Name</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${mySpecialties}" var="mySpecialty">
					<tr>
					<td><c:out value=" ${mySpecialty.name}" /></td>
					<td width="5%" align="center">
						<spring:url value="/specialty/vet/remove/{mySpecialtyId}" var="mySpecialtyUrlRead">
							<spring:param name="mySpecialtyId" value="${mySpecialty.id}" />
						</spring:url>
						<a href="${fn:escapeXml(mySpecialtyUrlRead)}">
						<span class="glyphicon glyphicon-minus-sign" aria-hidden="true"></span></a>
					</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<c:if test="${empty specialties}">
		<h3>don't have available specialties by the moment...</h3>
	</c:if>

	<c:if test="${not empty specialties}">
		<h3>Other Specialties</h3>
		<table id="specialtiesTable" class="table">
			<thead>
				<tr>
					<th>Name</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${specialties}" var="specialty">
					<tr>
					<td><c:out value=" ${specialty.name}" /></td>
					<td width="5%" align="center">
						<spring:url value="/specialty/vet/add/{specialtyId}" var="specialtyUrlRead">
							<spring:param name="specialtyId" value="${specialty.id}" />
						</spring:url>
						<a href="${fn:escapeXml(specialtyUrlRead)}">
						<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
					</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	
</petclinic:layout>