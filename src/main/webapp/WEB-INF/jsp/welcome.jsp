<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
    <h2><fmt:message key="welcome.title"/></h2>
    <h3><fmt:message key="welcome.message"/></h3>
    <div class="row">
        <div class="col-md-12">
            <!-- <spring:url value="/resources/images/pets.png" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" src="${petsImage}"/> -->
        </div>
    </div>
	<!--<div class="container">
	 <div class="jumbotron" style="border-radius:10px;background-color:white">
      <div class="container">
        <h1 class="display-3"><fmt:message key="welcome.title"/></h1>
        <p><fmt:message key="welcome.message"/></p>
      </div>
    </div>-->
	 <div style="border-radius: 10px;margin-top:25px" id="myCarousel" class="carousel slide" data-ride="carousel">
	   <!-- Indicators -->
	   <ol class="carousel-indicators">
	     <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
	     <li data-target="#myCarousel" data-slide-to="1"></li>
	     <li data-target="#myCarousel" data-slide-to="2"></li>
	   </ol>
	
	   <!-- Wrapper for slides -->
	   <div style="border-radius: 10px;" class="carousel-inner">
	     <div class="item active">
	       <img src="resources/images/slider-1.png" alt="Slider 1" style="width:100%;">
	     </div>
	
	     <div class="item">
	       <img src="resources/images/slider-2.png" alt="Slider 2" style="width:100%;">
	     </div>
	    
	     <div class="item">
	       <img src="resources/images/slider-3.png" alt="Slider 3" style="width:100%;">
	     </div>
	   </div>
	
	   <!-- Left and right controls -->
	   <a style="border-radius: 10px" class="left carousel-control" href="#myCarousel" data-slide="prev">
	     <span class="glyphicon glyphicon-chevron-left"></span>
	     <span class="sr-only">Previous</span>
	   </a>
	   <a style="border-radius: 10px" class="right carousel-control" href="#myCarousel" data-slide="next">
	     <span class="glyphicon glyphicon-chevron-right"></span>
	     <span class="sr-only">Next</span>
	   </a>
	 </div>
    </div>
    
</petclinic:layout>
