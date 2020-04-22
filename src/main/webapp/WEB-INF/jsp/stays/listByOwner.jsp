<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{ownerId}">

		<h2>Accepted Stays</h2>
   		<c:if test="${not empty staysAccepted}">
        	<table id="staysTable" class="table table-striped">
        	<thead>
        	<tr>
            	<th>Start Date</th>
            	<th>Finish Date</th>
            	<th>Description</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${staysAccepted}" var="stayAccepted">
            		<tr>
                		<td>
                   			<c:out value="${stayAccepted.startDate}"/>
                		</td>
                		<td>
                   			<c:out value="${stayAccepted.finishDate}"/>
                		</td>
                		<td>
                    		<c:out value="${stayAccepted.description}"/>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty staysAccepted}">
    		<h3>You don't have Accepted Stays by the moment...</h3>
    	</c:if>

		<h2>Stays waiting for Acceptance</h2>
		<c:if test="${not empty staysPending}">
        	<table id="staysTable" class="table table-striped">
        	<thead>
        	<tr>
            	<th>Start Date</th>
            	<th>Finish Date</th>
            	<th>Description</th>
       		</tr>
        	</thead>
      		<tbody>
       			<c:forEach items="${staysPending}" var="stayPending">
            		<tr>
                		<td>
                   			<c:out value="${stayPending.startDate}"/>
                		</td>
                		<td>
                    		<c:out value="${stayPending.finishDate}"/>
                		</td>
                		<td>
                    		<c:out value="${stayPending.description}"/>
                		</td>
            		</tr>
        		</c:forEach>
        	</tbody>
    		</table>
    	</c:if>
    	
    	<c:if test="${empty staysPending}">
    		<h3>You don't have Stays waiting for being accepted by the moment...</h3>
    	</c:if>
 
</petclinic:layout>