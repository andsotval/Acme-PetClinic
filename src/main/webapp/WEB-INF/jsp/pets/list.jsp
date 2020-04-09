<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pets">
    <h2>My Pets</h2>

    <table id="petsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Type</th>
            <th>Birth Date</th>
            <th>New visit</th>
            <th>New stay</th>
            <th>Delete</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${pets}" var="pet">
            <tr>
                <td>
                    <c:out value="${pet.name}"/>
                </td>
                <td>
                    <c:out value="${pet.type}"/>
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
	                <span class="glyphicon glyphicon-bed" aria-hidden="true" ></span>
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
    
    <table class="table-buttons">
        <tr>
            <td>
            	<a class="btn btn-default" href='<spring:url value="/pets/new/${ownerId}" htmlEscape="true"/>'>Add new Pet</a>
            </td>            
        </tr>
    </table>
</petclinic:layout>