<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="providers">
	<h2>Add Provider</h2>

	<table class="table table-striped">
		<tr>
			<th>Name</th>
			<td><b><c:out
						value="${provider.firstName} ${provider.lastName}" /></b></td>
		</tr>
		<tr>
			<th>Address</th>
			<td><c:out value="${provider.address}" /></td>
		</tr>
		<tr>
			<th>City</th>
			<td><c:out value="${provider.city}" /></td>
		</tr>
		<tr>
			<th>Telephone</th>
			<td><c:out value="${provider.telephone}" /></td>
		</tr>
		<tr>
			<th>Mail</th>
			<td><c:out value="${provider.mail}" /></td>
		</tr>
	</table>

	<form:form modelAttribute="provider" class="form-horizontal"
		id="add-provider-form">
		<div class="form-group has-feedback">
			<input type="hidden" name="id" value="${provider.id}" />
			<input type="hidden" name="user.id" value="${provider.user.id}" />
			<input type="hidden" name="user.enabled" value="${provider.user.enabled}" />
			<input type="hidden" name="manager.id" value="${provider.manager.id}" />
			<input type="hidden" name="firstName" value="${provider.firstName}" />
			<input type="hidden" name="lastName" value="${provider.lastName}" />
			<input type="hidden" name="address" value="${provider.address}" />
			<input type="hidden" name="city" value="${provider.city}" />
			<input type="hidden" name="telephone" value="${provider.telephone}" />
			<input type="hidden" name="mail" value="${provider.mail}" />
			<input type="hidden" name="user.username" value="${provider.user.username}" />
			<input type="hidden" name="user.password" value="${provider.user.password}" />
		</div>
		<!--  <div class="form-group has-feedback">
			<petclinic:inputField label="First Name" name="firstName" />
			<petclinic:inputField label="Last Name" name="lastName" />
			<petclinic:inputField label="Address" name="address" />
			<petclinic:inputField label="City" name="city" />
			<petclinic:inputField label="Telephone" name="telephone" />
			<petclinic:inputField label="Mail" name="mail" />
			<petclinic:inputField label="Username" name="user.username" />
			<petclinic:inputField label="Password" name="user.password" />
		</div> -->

		<div class="form-group">
			<div class="col-sm-offset-2 col-sm-10">
				<button class="btn btn-default" type="submit">Add Provider
					to Manager</button>
			</div>
		</div>
	</form:form>
</petclinic:layout>
