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
            <th>Accept</th>
            <th>Decline</th>
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
                	<spring:url value="/stays/accept/{stayId}" var="stayAcceptUrl">
                        <spring:param name="stayId" value="${stay.id}"/>
                    </spring:url>
                	<a href="${fn:escapeXml(stayAcceptUrl)}">Accept</a>
                </td>
                 <td>
                	<spring:url value="/stays/decline/{stayId}" var="stayDeclineUrl">
                        <spring:param name="stayId" value="${stay.id}"/>
                    </spring:url>
                	<a href="${fn:escapeXml(stayDeclineUrl)}">Decline</a>
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