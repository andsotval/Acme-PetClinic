<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!--  >%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%-->
<%@ attribute name="name" required="true" rtexprvalue="true"
	description="Name of the active menu: home, owners, vets or error"%>

<nav class="navbar navbar-default" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand"
				href="<spring:url value="/" htmlEscape="true" />"><span></span></a>
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#main-navbar">
				<span class="sr-only"><os-p>Toggle navigation</os-p></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div class="navbar-collapse collapse" id="main-navbar">
			<ul class="nav navbar-nav">

				<petclinic:menuItem active="${name eq 'home'}" url="/"
					title="home page">
					<span class="glyphicon glyphicon-home" aria-hidden="true"></span>
					<span>Home</span>
				</petclinic:menuItem>

				<%-- <petclinic:menuItem active="${name eq 'owners'}" url="/owners/find"
					title="find owners">
					<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
					<span>Find owners</span>
				</petclinic:menuItem> --%>

				<sec:authorize access="hasAuthority('admin')">
				<petclinic:menuItem active="${name eq 'pettypes'}" url="/pettype/listAvailable"
					title="pettypes">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Pet types</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'specialties'}" url="/specialty/listAvailable"
					title="specialties">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Specialties</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'suggestions'}" url="/suggestion/admin/list"
					title="suggestions">
					<span class="glyphicon glyphicon-bell" aria-hidden="true"></span>
					<span>Suggestions</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('manager')">
				<petclinic:menuItem active="${name eq 'vets'}" url="/vets/vetsAvailable"
					title="veterinarians">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Veterinarians</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'providers'}" url="/providers/listAvailable"
					title="providers">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Providers</span>
         		</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'orders'}" url="/orders/list"
					title="orders">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Orders</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('veterinarian')">
				<petclinic:menuItem active="${name eq 'visits'}" url="/visits/listAllPending"
					title="visits">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Visits</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'stays'}" url="/stays/listAllPending"
					title="stays">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>Stays</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('veterinarian')">
				<petclinic:menuItem active="${name eq 'clinics'}" url="/clinics/getDetail"
					title="stays">
					<span class="glyphicon glyphicon-briefcase" aria-hidden="true"></span>
					<span>My Clinic</span>
				</petclinic:menuItem>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('owner')">
				<petclinic:menuItem active="${name eq 'pets'}" url="/pets/listMyPets"
					title="pets">
					<span class="glyphicon glyphicon-th-list" aria-hidden="true"></span>
					<span>My Pets</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'clinics'}" url="/clinics/owner"
					title="Clinic">
					<span class="glyphicon glyphicon-briefcase" aria-hidden="true"></span>
					<span>Clinic</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'visits'}" url="/visits/listByOwner"
					title="visits">
					<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
					<span>My Visits</span>
				</petclinic:menuItem>
				<petclinic:menuItem active="${name eq 'stays'}" url="/stays/listByOwner"
					title="stays">
					<span class="glyphicon glyphicon-list-alt" aria-hidden="true"></span>
					<span>My Stays</span>
				</petclinic:menuItem>
				</sec:authorize>
				
        
		        <sec:authorize access="isAuthenticated() and !hasAuthority('admin')">
						<petclinic:menuItem active="${name eq 'suggestions'}" url="/suggestion/user/list"
							title="suggestions">
					<span class="glyphicon glyphicon-bullhorn" aria-hidden="true"></span>
					<span>Suggestions</span>
		         </petclinic:menuItem>
				</sec:authorize>

			</ul>




			<ul class="nav navbar-nav navbar-right">
				<sec:authorize access="!isAuthenticated()">
					<li><a href="<c:url value="/login" />">Login</a></li>
					<li><a href="<c:url value="/users/new" />">Register</a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"> <span class="glyphicon glyphicon-user"></span> 
							<strong><sec:authentication property="name" /></strong> <span
							class="glyphicon glyphicon-chevron-down"></span>
					</a>
						<ul class="dropdown-menu">
							<li>
								<div class="navbar-login">
									<div class="row">
										<div class="col-lg-4">
											<p class="text-center">
												<span class="glyphicon glyphicon-user icon-size"></span>
											</p>
										</div>
										<div class="col-lg-8">
											<p class="text-left">
												<strong><sec:authentication property="name" /></strong>
											</p>
											<p class="text-left">
												<a href="<c:url value="/logout" />"
													class="btn btn-primary btn-block btn-sm">Logout</a>
											</p>
										</div>
									</div>
								</div>
							</li>
							<li class="divider"></li>
<!-- 							
                            <li> 
								<div class="navbar-login navbar-login-session">
									<div class="row">
										<div class="col-lg-12">
											<p>
												<a href="#" class="btn btn-primary btn-block">My Profile</a>
												<a href="#" class="btn btn-danger btn-block">Change
													Password</a>
											</p>
										</div>
									</div>
								</div>
							</li>
-->
						</ul></li>
				</sec:authorize>
			</ul>
		</div>



	</div>
</nav>
