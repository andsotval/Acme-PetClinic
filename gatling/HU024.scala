package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU024 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.petclinic.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")
		
	object Home{
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
	}
	object Login{
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("Login_2")
			.get("/login")
			.headers(headers_2)))
		.pause(8)
		.exec(http("LoggedAsManager")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "manager1")
			.formParam("password", "manager1")
			.formParam("_csrf", "648ea219-e8bb-4480-86a2-0a80319edaa9"))
		.pause(24)
	}
	
	object ShowAvailableProviders{
		val showAvailableProviders = exec(http("ShowAvailableProviders")
			.get("/providers/listAvailable")
			.headers(headers_0))
		.pause(30)
	}
	
	object ShowProductsOfProvider{
		val showProductsOfProvider = exec(http("ShowProductsOfProvider")
			.get("/providers/listProductsByProvider/3")
			.headers(headers_0))
		.pause(30)
	}
	
	object AddProvidersToClinic{
		val addProvidersToClinic = exec(http("ShowAvailableProviders")
			.get("/providers/listAvailable")
			.headers(headers_0))
		.pause(6)
		.exec(http("AddProvidersToClinic")
			.get("/providers/addProvider/3")
			.headers(headers_0))
		.pause(14)
	}

	val scnHU024_AddProvidersToClinic = scenario("scnHU024_AddProvidersToClinic").exec(
		Home.home,
		Login.login,
		ShowAvailableProviders.showAvailableProviders,
		ShowProductsOfProvider.showProductsOfProvider,
		AddProvidersToClinic.addProvidersToClinic
	)

	setUp(
		scnHU024_AddProvidersToClinic.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}