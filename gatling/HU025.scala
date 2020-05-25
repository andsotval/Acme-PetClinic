package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU025 extends Simulation {

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
		.pause(9)
	}
	object Login{
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("Login_2")
			.get("/login")
			.headers(headers_2)))
		.pause(6)
		.exec(http("LoggedAsManager")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "manager1")
			.formParam("password", "manager1")
			.formParam("_csrf", "cbb09606-eb61-459d-b8ec-2df7339c20f6"))
		.pause(10)
	}
	
	object ShowAvailableVets{
	 val showAvailableVets = exec(http("ShowAvailableVets")
			.get("/vets/vetsAvailable")
			.headers(headers_0))
		.pause(17)
	}
	
	object AddVetsToClinic{
	 val addVetsToClinic = exec(http("AddVet1ToClinic")
			.get("/vets/accept/6")
			.headers(headers_0))
		.pause(19)
		.exec(http("AddVet1ToClinic")
			.get("/vets/accept/7")
			.headers(headers_0))
		.pause(2)
	}

	val scnHU025_AddVetsToClinic = scenario("scnHU025_AddVetsToClinic").exec(
		Home.home,
		Login.login,
		ShowAvailableVets.showAvailableVets,
		AddVetsToClinic.addVetsToClinic
	)

	setUp(
		scnHU025_AddVetsToClinic.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}