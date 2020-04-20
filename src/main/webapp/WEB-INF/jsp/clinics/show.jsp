<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="clinic/getDetail">
    <h2>Clinic</h2>

    <table id="clinicTable" class="table table-striped">
        <thead>
        <tr>
            <th>Name</th>
            <th>Address</th>
            <th>City</th>
            <th>Telephone</th>
            <th>Manager</th>        
        </tr>
        </thead>
        <tbody>
            <tr>
                <td>
                    <c:out value="${clinic.name}"/>
                </td>
                <td>
                    <c:out value="${clinic.address} "/>
                </td>
                <td>
                    <c:out value="${clinic.city} "/>
                </td>
                <td>
                    <c:out value="${clinic.telephone} "/>
                </td>
                <td>
                    <c:out value="${clinic.manager.firstName} "/>
                </td>
            </tr>
        </tbody>
    </table>

	
</petclinic:layout>
