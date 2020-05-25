<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="users">
    <h2>
        <c:if test="${person['new']}">New </c:if> User
    </h2>
    <form:form modelAttribute="person" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="First Name" name="firstName"/>
            <petclinic:inputField label="Last Name" name="lastName"/>
            <petclinic:inputField label="Address" name="address"/>
            <petclinic:inputField label="City" name="city"/>
            <petclinic:inputField label="Telephone" name="telephone"/>
            <petclinic:inputField label="Email" name="mail"/>
            <petclinic:inputField label="Username" name="user.username"/>
            <petclinic:inputPassword label="Password" name="user.password"/>
            <input type="hidden" name="user.enabled" value="true"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${person['new']}">
	                        <button class="btn btn-default" type="submit" formaction="/users/new?role=owner">Add Owner</button>
	                        <button class="btn btn-default" type="submit" formaction="/users/new?role=vet">Add Vet</button>
	                        <button class="btn btn-default" type="submit" formaction="/users/new?role=manager">Add Manager</button>
	                        <button class="btn btn-default" type="submit" formaction="/users/new?role=provider">Add Provider</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update User</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
