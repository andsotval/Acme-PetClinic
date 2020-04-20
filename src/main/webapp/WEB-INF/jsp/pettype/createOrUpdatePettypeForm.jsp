<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="pettype">
    <h2>
        <c:if test="${pettype['new']}">New </c:if>Pet Type
    </h2>
    <form:form modelAttribute="pettype" class="form-horizontal" id="create-pettype-form">
    
    	<input type="hidden" name="id" value="${pettype.id}"/>
    	<input type="hidden" name="available" value="true"/>
    	
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<c:choose>
                    <c:when test="${pettype['new']}">
						<button class="btn btn-default" type="submit">Create Pet Type</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Pet Type</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>