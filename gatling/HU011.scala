package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU011 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.png""", """.*.ico"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_1 = Map("Origin" -> "http://www.petclinic.com")

	object Login{
		val login = exec(http("Login")
			.get("/login"))
		.pause(16)
		.exec(http("request_1")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "manager1")
			.formParam("password", "manager1")
			.formParam("_csrf", "5753f5a9-f779-4780-a25a-27a496f37e35"))
		.pause(23)
	}

	object ListProviders{
		var listProviders = exec(http("ListProviders")
			.get("/providers/listAvailable"))
		.pause(19)
	}

	object ShowProviderDetails{
		var showProviderDetails = exec(http("ShowProviderDetails")
			.get("/providers/listProductsByProvider/3"))
		.pause(11)
	}

	val scnHU011_ShowProviderDetails = scenario("HU011").exec(
		Login.login,
		ListProviders.listProviders,
		ShowProviderDetails.showProviderDetails
	)

	setUp(
		scnHU011_ShowProviderDetails.inject(rampUsers(10000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	)
}