package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU012 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources()
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.petclinic.com")


	object Home{
		val home = exec(http("Home")
			.get("/"))
		.pause(1)
	}
	object Login{
		val login = exec(http("Login")
			.get("/login"))
		.pause(23)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "owner1")
			.formParam("password", "owner1")
			.formParam("_csrf", "2cab78f5-7542-453d-bf98-9865c83c21ac"))
		.pause(33)
	}
	object ListStays{
		val listStays = exec(http("ListStays")
			.get("/stays/listByOwner"))
		.pause(7)
	}


	val scnHU012_ShowStays = scenario("HU012").exec(
		Home.home,
		Login.login,
		ListStays.listStays
	)

	setUp(
		scnHU011_AddNewPet.inject(rampUsers(5000) during (100 seconds)),
		scnHU012_deletePet.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	)
}