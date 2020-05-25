package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU023 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
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
		"Origin" -> "http://www.dp2.com",
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
		.pause(10)
		.exec(http("LoggedAsOwner")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner10")
			.formParam("password", "owner10")
			.formParam("_csrf", "64af2d94-afb4-4dae-b353-5199c1309e23"))
		.pause(8)
	}
	
	object ShowClinicsAvailable{
		val showClinicsAvailable = exec(http("ShowClinicsAvailable")
			.get("/clinics/owner")
			.headers(headers_0))
		.pause(18)
	}
	
	object ShowClinic{
		val showClinic = exec(http("ShowClinic")
			.get("/clinics/owner/1")
			.headers(headers_0))
		.pause(23)
	}
	
	object SubscribeToClinic{
		val subscribeToClinic = exec(http("SubscribeToClinic")
			.get("/clinics/owner/subscribeToClinic/1")
			.headers(headers_0))
		.pause(13)
	}
	
	val scnHU023_SubscribeToClinic = scenario("scnHU023_SubscribeToClinic").exec(
		Home.home,
		Login.login,
		ShowClinicsAvailable.showClinicsAvailable,
		ShowClinic.showClinic,
		SubscribeToClinic.subscribeToClinic
	)

	setUp(
		scnHU023_SubscribeToClinic.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}