<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{vetId}">

	<c:if test="${accepted eq true}">
		<h2>Accepted Visits</h2>
	</c:if>
	<c:if test="${accepted eq false}">
		<h2>Pending Visits</h2>
	</c:if>
	
	<div class="form-group" style="color: green;">
		<c:out value="${message}" />
	</div>

	<c:if test="${not empty visits}">
    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Status</th>
            <c:if test="${accepted eq true}">
				 <th>Modify</th>
			</c:if>
			<c:if test="${accepted eq false}">
				 <th>Accept</th>
			</c:if>
            <th>Reject</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${visits}" var="visit">
            <tr>
                <td width="20%"><petclinic:localDateTime date="${visit.dateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                		
                </td>
                <td width="35%">
                    <c:out value="${visit.description} "/>
                </td>
                <td width="15%">
			       <c:choose>
                		<c:when test="${visit.isAccepted == null}">
                			<c:out value="PENDING"/>
                		</c:when>
                		<c:when test="${visit.isAccepted eq true}">
                			<c:out value="ACCEPTED"/>
                		</c:when>
                		<c:otherwise>
                			<c:out value="REJECTED"/>
                		</c:otherwise>
                	</c:choose>
			    </td>
                <td width="15%">
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
                </td>
                <td width="15%">
                	<spring:url value="/visits/cancel/{visitId}" var="visitUrlCancel">
	                    <spring:param name="visitId" value="${visit.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(visitUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
	</c:if>
	

	<c:if test="${accepted == false}">
		<c:if test="${empty visits}">
			<h3>You have checked out all the pending visits</h3>
		</c:if> 
		<a class="btn btn-default" href='<spring:url value="/visits/listAllAccepted" htmlEscape="true"/>'>List my accepted visits</a>
	</c:if>
	
	<c:if test="${accepted == true}"> 
		<c:if test="${empty visits}">
			<h3>You don't have accepted visits</h3>
		</c:if> 
		<a class="btn btn-default" href='<spring:url value="/visits/listAllPending" htmlEscape="true"/>'>List my pending visits</a>
	</c:if>
    <table class="table-buttons">
        <tr>
            <td>
            
            </td>            
        </tr>
    </table>
</petclinic:layout>
