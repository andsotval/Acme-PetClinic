<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="stays">
    <h2>History</h2>

    <table id="staysTable" class="table table-striped">
        <thead>
        <tr>
            <th>Start Date</th>
            <th>Finish Date</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${stays}" var="stay">
            <tr>
                <td>
                    <c:out value="${stay.startDate}"/>
                </td>
                <td>
                    <c:out value="${stay.finishDate}"/>
                </td>
                <td>
                    <c:out value="${stay.description} "/>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</petclinic:layout>