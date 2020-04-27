 <%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<petclinic:layout pageName="suggestion">
	<h2>Suggestions send</h2>

	<c:if test="${empty suggestions}">
		<h3>You don't have suggestions by the moment...</h3>
	</c:if>

	<c:if test="${not empty suggestions}">
	<table id="staysTable" class="table table-striped">
        <thead>
        <tr>
            <th>Title</th>
            <th>Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${suggestions}" var="suggestion">
        	<tr>
                <td>
                	<spring:url value="/suggestion/user/details/{suggestionId}" var="suggestionUrlDetail">
						<spring:param name="suggestionId" value="${suggestion.id}" />
					</spring:url> 
					<a href="${fn:escapeXml(suggestionUrlDetail)}"><span class="glyphicon glyphicon-option-vertical" aria-hidden="true"></span><strong><c:out value=" ${suggestion.name}"/></strong></a>
                </td>
                <td width="20%">
                    <petclinic:localDateTime date="${suggestion.created}" pattern="yyyy-MM-dd HH:mm:ss" />
                </td>
                <td width="5%" align="center">
                		<spring:url value="/suggestion/user/delete/{suggestionId}" var="suggestionUrlMoveTrash">
								<spring:param name="suggestionId" value="${suggestion.id}" />
						</spring:url>
						<a href="${fn:escapeXml(suggestionUrlMoveTrash)}" > 
							<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
						</a>
				</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

	<a class="btn btn-default" href='<spring:url value="/suggestion/user/deleteAll" htmlEscape="true"/>'>Remove All Suggestions</a>
	</c:if>
			
	<a class="btn btn-default" href='<spring:url value="/suggestion/user/new" htmlEscape="true"/>'>New Suggestion</a>
    
</petclinic:layout>