<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="stays">

    <c:if test="${accepted eq true}">
		<h2>Accepted Stays</h2>
	</c:if>
	<c:if test="${accepted eq false}">
		<h2>Pending Stays</h2>
	</c:if>
	
	<c:if test="${not empty stays}">
    <table id="staysTable" class="table table-striped">
        <thead>
        <tr>
            <th>Start Date</th>
            <th>Finish Date</th>
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
        <c:forEach items="${stays}" var="stay">
            <tr>
                <td width="15%">
                    <c:out value="${stay.startDate}"/>
                </td>
                <td width="15%">
                    <c:out value="${stay.finishDate}"/>
                </td >
                <td width="25%">
                    <c:out value="${stay.description} "/>
                </td>
                <td width="15%">
			       <c:choose>
                		<c:when test="${stay.isAccepted == null}">
                			<c:out value="PENDING" />
                		</c:when>
                		<c:when test="${stay.isAccepted eq true}">
                			<c:out value="ACCEPTED" />
                		</c:when>
                		<c:otherwise>
                			<c:out value="REJECTED" />
                		</c:otherwise>
                	</c:choose>                
                </td>
                <td width="15%">
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
                	</td>
	           <td width="15%">
	           <spring:url value="/stays/cancel/{stayId}" var="stayUrlCancel">
	                    <spring:param name="stayId" value="${stay.id}"/>
	                </spring:url>
	           <a href="${fn:escapeXml(stayUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
	           </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </c:if>

    
    <table class="table-buttons">
        <tr>
            <td>
                <c:if test="${accepted == false}"> 
                	<c:if test="${empty stays}">
						<h3>You have checked out all the pending stays</h3>
					</c:if> 
					<a class="btn btn-default" href='<spring:url value="/stays/listAllAccepted" htmlEscape="true"/>'>List my accepted stays</a>
				</c:if>
	
				<c:if test="${accepted == true}"> 
					<c:if test="${empty stays}">
						<h3>You don't have accepted stays</h3>
					</c:if> 
					<a class="btn btn-default" href='<spring:url value="/stays/listAllPending" htmlEscape="true"/>'>List my pending stays</a>
				</c:if>
            </td>            
        </tr>
    </table>
</petclinic:layout>