<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="visits/{ownerId}">

   		<c:if test="${staysAccepted != null}">
        	<h2>Accepted Stays</h2>
        
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

		<c:if test="${staysPending != null}">
        	<h2>Stays waiting for Acceptance</h2>
        
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
 
</petclinic:layout>