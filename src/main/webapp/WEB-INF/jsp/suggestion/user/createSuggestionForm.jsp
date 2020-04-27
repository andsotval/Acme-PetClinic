<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="suggestion">
    <h2>
        New Suggestion
    </h2>
    <form:form modelAttribute="suggestion" class="form-horizontal" id="create-suggestion-form" action="/suggestion/user/save">
		<input type="hidden" name="created" value="<petclinic:localDateTime date="${suggestion.created}" pattern="yyyy/MM/dd HH:mm:ss" />"/>
		<input type="hidden" name="isRead" value="${suggestion.isRead}"/>
		<input type="hidden" name="isTrash" value="${suggestion.isTrash}"/>
		<input type="hidden" name="isAvailable" value="${suggestion.isAvailable}"/>
		<input type="hidden" name="user" value="${suggestion.user.id}"/>
		
		<petclinic:inputField label="Title" name="name" />
		<petclinic:textareaField label="Description" name="description" />

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
            	<a class="btn btn-default" href='<spring:url value="/suggestion/user/list" htmlEscape="true"/>'>Back to Suggestions Sent</a>   
                <button class="btn btn-default" type="submit">Create Suggestion</button>
            </div>
        </div>
    </form:form>
    
</petclinic:layout>