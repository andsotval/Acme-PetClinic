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
			<td><petclinic:localDateTime date="${suggestion.created}" pattern="yyyy/MM/dd HH:mm:ss" /></td>
		</tr>
		<tr>
			<th>Description</th>
			<td><c:out value="${suggestion.description}" /></td>
		</tr>
	</table>

	<a class="btn btn-default" href='<spring:url value="/suggestion/user/delete/${suggestion.id}" htmlEscape="true"/>'>Remove
		Suggestion</a>
	<a class="btn btn-default" href='<spring:url value="/suggestion/user/list" htmlEscape="true"/>'>Back to Suggestions Sent</a>

</petclinic:layout>
