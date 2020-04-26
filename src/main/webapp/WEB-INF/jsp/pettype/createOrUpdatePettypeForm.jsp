<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="petType">
    <h2>
        <c:if test="${petType['new']}">New </c:if>Pet Type
    </h2>
    <form:form modelAttribute="petType" class="form-horizontal" id="create-pettype-form">
    
    	<input type="hidden" name="id" value="${petType.id}"/>
    	<input type="hidden" name="available" value="${petType.available}"/>
    	
        <petclinic:inputField label="Name" name="name"/>
        
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<c:choose>
                    <c:when test="${petType.available eq true}">
						<a class="btn btn-default" href='<spring:url value="/pettype/listAvailable" htmlEscape="true"/>'>Back to Pet Type Available List</a> 
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-default" href='<spring:url value="/pettype/listNotAvailable" htmlEscape="true"/>'>Back to Pet Type Not Available List</a> 
                    </c:otherwise>
                </c:choose>
            	<c:choose>
                    <c:when test="${petType['new']}">
						<button class="btn btn-default" type="submit" formaction="/pettype/new">Create Pet Type</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit" formaction="/pettype/edit/${pettypeId}">Update Pet Type</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>