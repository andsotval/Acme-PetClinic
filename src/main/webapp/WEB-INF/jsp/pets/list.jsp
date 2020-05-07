<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pets">
    <h2>My Pets</h2>
    
    <div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>
    
    <c:if test="${empty pets}">
		<h3>You don't have pets by the moment...</h3>
	</c:if>
	
	<c:if test="${not empty pets}">
	    <table id="petsTable" class="table table-striped">
	        <thead>
	        <tr>
	            <th>Name</th>
	            <th>Type</th>
	            <th>Birth Date</th>
	            <th>Visits</th>
	            <th>Stays</th>
	            <th>Delete</th>
	        </tr>
	        </thead>
	        <tbody>
	        <c:forEach items="${pets}" var="pet">
	            <tr>
	                <td>
	                    <spring:url value="/pets/{petId}/edit" var="petUrl">
	                        <spring:param name="petId" value="${pet.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(petUrl)}"><c:out value="${pet.name}"/></a>
	                </td>
	                <td>
	                    <c:out value="${pet.type.name}"/>
	                </td>
	                <td>
	                    <c:out value="${pet.birthDate} "/>
	                </td>
	                 <td>
	                 <spring:url value="/pets/newVisit/{petId}" var="petUrlNewVisit">
		                    <spring:param name="petId" value="${pet.id}"/>
		                </spring:url>
		                <a href="${fn:escapeXml(petUrlNewVisit)}"><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a>
	                </td>
	                 <td>
		                <spring:url value="/pets/newStay/{petId}" var="petUrlNewStay">
		                    <spring:param name="petId" value="${pet.id}"/>
		                </spring:url>
		                <a href="${fn:escapeXml(petUrlNewStay)}"><span class="glyphicon glyphicon-bed" aria-hidden="true"></span></a>
	                </td>
	                <td>
	                	<spring:url value="/pets/delete/{petId}" var="petUrlCancel">
		                    <spring:param name="petId" value="${pet.id}"/>
		                </spring:url>
		                <a href="${fn:escapeXml(petUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
	                </td>
	            </tr>
	        </c:forEach>
	        </tbody>
	    </table>
    </c:if>
    
    <table class="table-buttons">
        <tr>
            <td>
            	<a class="btn btn-default" href='<spring:url value="/pets/new" htmlEscape="true"/>'>Add new Pet</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>