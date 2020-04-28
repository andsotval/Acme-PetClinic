<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{ownerId}">

		<h1>My Visits</h1>
		<h2>Accepted Visits</h2>
   		<c:if test="${not empty visitsAccepted}">
        	<table id="visitsTable" class="table table-striped">
        	<thead>
        	<tr>
        		<th>Pet Name</th>
            	<th>Date</th>
            	<th>Description</th>
            	<th>Status</th>
            	<th>Cancel</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${visitsAccepted}" var="visitAccepted">
            		<tr>
            		    <td width="15%">
                   			<c:out value="${visitAccepted.pet.name}"/>
                		</td>
                		<td width="30%">
                   			<petclinic:localDateTime date="${visitAccepted.dateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                		</td>
                		<td width="30%">
                    		<c:out value="${visitAccepted.description}"/>
                		</td>
                		<td width="15%">
                			<c:out value="ACCEPTED"/>
                		</td>
                		<td width="10%">
                		<spring:url value="/visits/cancel/{visitId}" var="visitUrlCancel">
	                    	<spring:param name="visitId" value="${visitAccepted.id}"/>
	                	</spring:url>
	                	<a href="${fn:escapeXml(visitUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty visitsAccepted}">
    		<h3>You don't have Accepted Visits by the moment</h3>
    	</c:if>

		<h2>Visits waiting for Acceptance</h2>
		<c:if test="${not empty visitsPending}">
        	<table id="visitsTable" class="table table-striped">
        	<thead>
        	<tr>
        		<th>Pet Name</th>
            	<th>Date</th>
            	<th>Description</th>
            	<th>Status</th>
            	<th>Cancel</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${visitsPending}" var="visitPending">
            		<tr>
            			<td width="15%">
                   			<c:out value="${visitPending.pet.name}"/>
                		</td>
                		<td width="30%">
                			<petclinic:localDateTime date="${visitPending.dateTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                		</td>
                		<td width="30%">
                    		<c:out value="${visitPending.description}"/>
                		</td>
                		<td width="15%">
                			<c:out value="PENDING"/>
                		</td>
                		<td width="10%">
                		<spring:url value="/visits/cancel/{visitId}" var="visitUrlCancel">
	                    	<spring:param name="visitId" value="${visitPending.id}"/>
	                	</spring:url>
	                	<a href="${fn:escapeXml(visitUrlCancel)}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></a>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty visitsPending}">
    		<h3>You don't have Visits waiting for being accepted by the moment</h3>
    	</c:if>
    	
 
</petclinic:layout>