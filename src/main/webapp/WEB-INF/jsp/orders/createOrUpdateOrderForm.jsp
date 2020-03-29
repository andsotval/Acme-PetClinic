<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="orders">
    <h2>
        <c:if test="${order['new']}">New </c:if> Order
    </h2>
    <form:form modelAttribute="order" class="form-horizontal" id="add-order-form">
    
    	<input type="hidden" name="id" value="${order.id}"/>
    	<input type="hidden" name="manager.id" value="${order.manager.id}"/>
    	<input type="hidden" name="isAccepted" value="false"/>
    	
        <div class="form-group has-feedback">
            <petclinic:inputField label="Name" name="name"/>
            <petclinic:inputField label="Date" name="date"/>
            <petclinic:selectField label="Products" name="products" size="10" names="${products}"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                
                    <c:when test="${order['new']}">
                        <button class="btn btn-default" type="submit">Create Order</button>
                    </c:when>
                    <!--<c:otherwise>
                        <button class="btn btn-default" type="submit">Update Owner</button>
                    </c:otherwise> -->
                
            </div>
        </div>
    </form:form>
</petclinic:layout>