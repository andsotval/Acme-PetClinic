<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="/providers">
	<h2>Available providers</h2>

	<div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>

	<c:if test="${empty availableProviders}">
		<h3>You don't have availables providers by the moment...</h3>
	</c:if>

	<c:if test="${not empty availableProviders}">
		<table id="providersTable" class="table table-striped">
			<thead>
				<tr>
					<th>Name</th>
					<th>Mail</th>
					<th>Telephone</th>					
					<th>Add provider</th>
					<th>See more</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${availableProviders}" var="avProvider">
					<tr>
						<td width="30%"><c:out
								value="${avProvider.firstName} ${avProvider.lastName}" /></td>
						<td width="30%"><c:out value="${avProvider.mail}" /></td>
						<td width="20%"><c:out value="${avProvider.telephone}" /></td>
						<td width="10%" align="center"><spring:url value="/providers/addProvider/{providerId}"
								var="providerUrl">
								<spring:param name="providerId" value="${avProvider.id}" />
							</spring:url> <a href="${fn:escapeXml(providerUrl)}"><span
								class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span></a>
						</td>
						<td width="10%" align="center"><spring:url
								value="/providers/listProductsByProvider/{providerId}"
								var="providerUrl">
								<spring:param name="providerId" value="${avProvider.id}" />
							</spring:url> <a href="${fn:escapeXml(providerUrl)}"><span
								class="glyphicon glyphicon-share-alt" aria-hidden="true"></span></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

	<h2>Already added providers</h2>

	<c:if test="${empty addedProviders}">
		<h3>You don't have providers by the moment...</h3>
	</c:if>

	<c:if test="${not empty addedProviders}">
		<table id="addedProvidersTable" class="table table-striped">
			<thead>
				<tr>
					<th>Name</th>
					<th>Mail</th>
					<th>Telephone</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${addedProviders}" var="addedProvider">
					<tr>
						<td width="30%"><c:out
								value="${addedProvider.firstName} ${addedProvider.lastName}" />
						</td>
						<td width="30%"><c:out value="${addedProvider.mail}" /></td>
						<td width="40%"><c:out value="${addedProvider.telephone}" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>

</petclinic:layout>
