<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<petclinic:layout pageName="suggestion">
	<c:choose>
		<c:when test="${isTrash eq false}">
			<h2>Suggestions Received</h2>
		</c:when>
		<c:otherwise>
			<h2>Suggestions Trash</h2>
		</c:otherwise>
	</c:choose>
	
	<div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>

	<c:if test="${empty suggestions}">
		<h3>You don't have suggestions by the moment...</h3>
		<c:choose>
			<c:when test="${isTrash eq false}">
				<a class="btn btn-default" href='<spring:url value="/suggestion/admin/listTrash" htmlEscape="true"/>'>List Trash</a>
			</c:when>
			<c:otherwise>
				<a class="btn btn-default" href='<spring:url value="/suggestion/admin/list" htmlEscape="true"/>'>List Received</a>
			</c:otherwise>
		</c:choose>
	</c:if>

	<c:if test="${not empty suggestions}">
	<table id="staysTable" class="table">
        <thead>
        <tr>
            <th>Title</th>
            <th>Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${suggestions}" var="suggestion">
        	<c:choose>
        		<c:when test="${suggestion.isRead eq false}">
        			<tr bgcolor="CBCBCB">
        		</c:when>
        		<c:otherwise>
        			<tr bgcolor="F1F0F0">
        		</c:otherwise>
        	</c:choose>
                <td>
                	<spring:url value="/suggestion/admin/details/{suggestionId}" var="suggestionUrlDetail">
						<spring:param name="suggestionId" value="${suggestion.id}" />
					</spring:url> 
					<a href="${fn:escapeXml(suggestionUrlDetail)}"><span class="glyphicon glyphicon-option-vertical" aria-hidden="true"></span><strong><c:out value=" ${suggestion.name}"/></strong></a>
                </td>
                <td width="20%">
                    <petclinic:localDateTime date="${suggestion.created}" pattern="yyyy-MM-dd HH:mm:ss" />
                </td>
                <td width="5%" align="center">
                	<c:if test="${isTrash eq false}">
	                	<c:choose>
		                	<c:when test="${suggestion.isRead eq false}">
			        			<spring:url value="/suggestion/admin/read/{suggestionId}" var="suggestionUrlRead">
									<spring:param name="suggestionId" value="${suggestion.id}" />
								</spring:url> 
								<a href="${fn:escapeXml(suggestionUrlRead)}"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a>
			        		</c:when>
			        		<c:otherwise>
			        			<spring:url value="/suggestion/admin/notRead/{suggestionId}" var="suggestionUrlNotRead">
									<spring:param name="suggestionId" value="${suggestion.id}" />
								</spring:url> 
								<a href="${fn:escapeXml(suggestionUrlNotRead)}"><span class="glyphicon glyphicon-eye-close" aria-hidden="true"></span></a>
			        		</c:otherwise>
		        		</c:choose>
                	<spring:url value="/suggestion/admin/moveTrash/{suggestionId}" var="suggestionUrlMoveTrash">
						<spring:param name="suggestionId" value="${suggestion.id}" />
					</spring:url> 
					<a href="${fn:escapeXml(suggestionUrlMoveTrash)}"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a>
                	</c:if>
                	<c:if test="${isTrash eq true}">
                		<spring:url value="/suggestion/admin/delete/{suggestionId}" var="suggestionUrlMoveTrash">
							<spring:param name="suggestionId" value="${suggestion.id}" />
						</spring:url>
						<a href="${fn:escapeXml(suggestionUrlMoveTrash)}" target="popup" 
								onclick="return confirm('Are you sure you want to delete the suggestion definitly?');">
								<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
						</a>
                	</c:if>
				</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

	<c:choose>
		<c:when test="${isTrash eq false}">
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/moveAllTrash" htmlEscape="true"/>'>Move Everything to the Trash</a>
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/listTrash" htmlEscape="true"/>'>List Trash</a>
		</c:when>
		<c:otherwise>
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/deleteAllTrash" htmlEscape="true"/>'
				target="popup" onclick="return confirm('Are you sure you want to delete all suggestions definitly?');">Remove All</a>
			<a class="btn btn-default" href='<spring:url value="/suggestion/admin/list" htmlEscape="true"/>'>List Received</a>
		</c:otherwise>
	</c:choose>
	</c:if>
    
</petclinic:layout>