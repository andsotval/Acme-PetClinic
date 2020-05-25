package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU022 extends Simulation {

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
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")
		
	object Home{
		val home = exec(http("Home")
			.get("/"))
		.pause(12)
	}
	object Login{
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("Login_2")
			.get("/login")
			.headers(headers_2)))
		.pause(9)
		.exec(http("LoggedAsOwner")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "owner1")
			.formParam("_csrf", "d18e4a20-4311-4dde-9451-d1aaa9316256"))
		.pause(10)
	}
	
	object ShowClinic{
		val showClinic = exec(http("ShowClinic")
			.get("/clinics/owner")
			.headers(headers_0))
		.pause(12)
	}
	
	object DeleteActiveVisits{
		val deleteActiveVisits = exec(http("ListActiveVisits")
			.get("/visits/listByOwner")
			.headers(headers_0))
		.pause(1)
		.exec(http("DeleteActiveVisits")
			.get("/visits/cancel/1")
			.headers(headers_0))
		.pause(1)
		.exec(http("ShowClinic")
			.get("/clinics/owner")
			.headers(headers_0))
		.pause(14)
	}
	
	object DeleteActiveStays{
		val deleteActiveStays = exec(http("ListActiveStays")
			.get("/stays/listByOwner")
			.headers(headers_0))
		.pause(3)
		.exec(http("DeleteActiveStays")
			.get("/stays/cancel/1")
			.headers(headers_0))
		.pause(1)
		.exec(http("ShowClinic")
			.get("/clinics/owner")
			.headers(headers_0))
		.pause(15)
	}
	
	object UnsubscribeFromClinic{
		val unsubscribeFromClinic = exec(http("UnsubscribeFromClinic")
			.get("/clinics/owner/unsubscribeFromClinic?clinicId=1")
			.headers(headers_0))
	}
	

	val scnHU022_UnsubscribeFromClinic = scenario("scnHU022_UnsubscribeFromClinic").exec(
		Home.home,
		Login.login,
		ShowClinic.showClinic,
		DeleteActiveVisits.deleteActiveVisits,
		ShowClinic.showClinic,		
		DeleteActiveStays.deleteActiveStays,
		ShowClinic.showClinic,
		UnsubscribeFromClinic.unsubscribeFromClinic
	)

	setUp(
		scnHU022_UnsubscribeFromClinic.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}