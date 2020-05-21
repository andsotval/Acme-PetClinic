<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"	uri="http://www.springframework.org/security/tags"%>


<petclinic:layout pageName="staysByPet">

	<jsp:body>
    	<c:if test="${product.id != null}">
				<h2>Update Product</h2>
        </c:if>
        
        <c:if test="${product.id == null}">
				<h2>New Product</h2>
        </c:if>
            
				<form:form modelAttribute="product" class="form-horizontal"
				action="/product/save">
						            
	         		<input type="hidden" name="id" value="${product.id}"/>
	         		<input type="hidden" name="provider" value="${product.provider.id}"/>
	         		<input type="hidden" name="available" value="${product.available}"/>

					<petclinic:inputField label="Name" name="name"/>
  					<petclinic:inputField label="Price" name="price" />
					<petclinic:inputField label="Tax" name="tax" />
					
					<div class="form-group">
			            <div class="col-sm-offset-2 col-sm-10">
			            	<a class="btn btn-default" href='<spring:url value="/product/myProductsList" htmlEscape="true"/>'>Back to My Products</a>   
			                <c:if test="${product.id != null}">
			                	<button class="btn btn-default" type="submit" formaction="/product/save/${stay.id}">Save Product</button>
			                </c:if>
			                <c:if test="${product.id == null}">
			                	<button class="btn btn-default" type="submit" formaction="/product/save">Create Product</button>
			                </c:if>
			            </div>
			        </div>
 		        </form:form>
      
    </jsp:body>

</petclinic:layout>