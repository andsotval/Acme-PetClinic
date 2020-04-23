<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="suggestion">

	<h2>Suggestion Information</h2>


	<table class="table table-striped">
		<tr>
			<th>Title</th>
			<td><c:out value="${suggestion.name}"/></td>
		</tr>
		<tr>
			<th>Date</th>
			<td><petclinic:localDateTime date="${suggestion.created}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
		</tr>
		<tr>
			<th>Description</th>
			<td><c:out value="${suggestion.description}" /></td>
		</tr>
	</table>
	
	<h2>${authority} Information</h2>


	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><c:out value="${person.firstName} ${person.lastName}"/></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${person.address}" /></td>
		</tr>
		<tr>
			<th>City</th>
			<td><c:out value="${person.city}" /></td>
		</tr>
		<tr>
			<th>Telephone</th>
			<td><c:out value="${person.telephone}" /></td>
		</tr>
		<tr>
			<th>Mail</th>
			<td><c:out value="${person.mail}" /></td>
		</tr>
	</table>
	
	<c:choose>
		<c:when test="${isTrash eq false}">
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/moveTrash/${suggestion.id}" htmlEscape="true"/>'>Move Suggestion to Trash</a>
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/list" htmlEscape="true"/>'>Back to Suggestion Received</a>
		</c:when>
		<c:otherwise>
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/delete/${suggestion.id}" htmlEscape="true" />'
				target="popup" onclick="return confirm('Are you sure you want to delete the suggestion definitly?');" >Remove Suggestion</a>
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/listTrash" htmlEscape="true"/>'>Back to Suggestion Trash</a>
		</c:otherwise>
	</c:choose>
	
</petclinic:layout>
