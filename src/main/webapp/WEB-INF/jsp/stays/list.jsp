<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="stays">
    <h2>Stays</h2>

    <table id="staysTable" class="table table-striped">
        <thead>
        <tr>
            <th>Start Date</th>
            <th>Finish Date</th>
            <th>Description</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${stays}" var="stay">
            <tr>
                <td>
                    <c:out value="${stay.startDate}"/>
                </td>
                <td>
                    <c:out value="${stay.finishDate}"/>
                </td>
                <td>
                    <c:out value="${stay.description} "/>
                </td>
                 <td>
                	<c:if test="${stay.isAccepted == null}">
	                	<spring:url value="/stays/accept/{stayId}" var="stayUrlAccept">
	                        <spring:param name="stayId" value="${stay.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(stayUrlAccept)}"><span class="glyphicon glyphicon-ok" aria-hidden="true" ></span></a>
	                </c:if>
	                <c:if test="${stay.isAccepted == true}">
	                	<spring:url value="/stays/changeDate/{stayId}" var="stayUrlChangeDate">
	                        <spring:param name="stayId" value="${stay.id}"/>
	                    </spring:url>
	                    <a href="${fn:escapeXml(stayUrlChangeDate)}"><span class="glyphicon glyphicon-time" aria-hidden="true" ></span></a>	
	                </c:if>
                	<spring:url value="/stays/cancel/{stayId}" var="stayUrlCancel">
	                    <spring:param name="stayId" value="${stay.id}"/>
	                </spring:url>
	                <a href="${fn:escapeXml(stayUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
	                
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    
    <c:if test="${accepted == false}"> 
		<a class="btn btn-default" href='<spring:url value="/stays/listAllAccepted" htmlEscape="true"/>'>List my accepted visits</a>
	</c:if>
	
	<c:if test="${accepted == true}"> 
		<a class="btn btn-default" href='<spring:url value="/stays/listAllPending" htmlEscape="true"/>'>List my pending visits</a>
	</c:if>
    
    <table class="table-buttons">
        <tr>
            <td>

            </td>            
        </tr>
    </table>
</petclinic:layout>