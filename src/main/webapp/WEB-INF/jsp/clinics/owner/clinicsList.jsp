<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="clinics">
	<h2>Clinics</h2>

	<c:if test="${empty clinics}">
		<h3>You don't have clinics available to subscribe...</h3>
	</c:if>

	<c:if test="${not empty clinics}">
		<table id="clinicsTable" class="table table-striped">
			<thead>
				<tr>
					<th>Name</th>
					<th>Address</th>
					<th>City</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${clinics}" var="clinic">
					<tr>
						<td width="30%"><spring:url value="/clinics/owner/{clinicId}"
								var="clinicUrl">
								<spring:param name="clinicId" value="${clinic.id}" />
							</spring:url> <a href="${fn:escapeXml(clinicUrl)}"><c:out
									value="${clinic.name}" /></a></td>
						<td width="55%"><c:out value="${clinic.address}" /></td>
						<td width="15%"><c:out value="${clinic.city}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
</petclinic:layout>