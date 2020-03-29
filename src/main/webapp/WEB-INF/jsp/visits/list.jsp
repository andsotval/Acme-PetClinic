<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{vetId}">
    <h2>Visits</h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${visits}" var="visit">
            <tr>
                <td>
                    <c:out value="${visit.date}"/>
                </td>
                <td>
                    <c:out value="${visit.description} "/>
                </td>
                <td>
                <c:if test="${visit.isAccepted == null}">
	                	<spring:url value="/visits/accept/{visitId}" var="visitUrlAccept">
	                        <spring:param name="visitId" value="${visit.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(visitUrlAccept)}"><span class="glyphicon glyphicon-ok" aria-hidden="true" ></span></a>
	                </c:if>
	                <c:if test="${visit.isAccepted == true}">
	                	<spring:url value="/visits/changeDate/{visitId}" var="visitUrlChangeDate">
	                        <spring:param name="visitId" value="${visit.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(visitUrlChangeDate)}"><span class="glyphicon glyphicon-time" aria-hidden="true" ></span></a>	
	                </c:if>
                	<spring:url value="/visits/cancel/{visitId}" var="visitUrlCancel">
	                    <spring:param name="visitId" value="${visit.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(visitUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
	                
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
        <tr>
            <td>

            </td>            
        </tr>
    </table>
</petclinic:layout>
