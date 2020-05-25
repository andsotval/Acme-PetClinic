package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU014 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.petclinic.com")

	object Home{
		val home = exec(http("Home")
			.get("/"))
		.pause(8)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(10)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "owner3")
			.formParam("password", "owner3")
			.formParam("_csrf", "${stoken}"))
			.pause(12)
	}

	object ListMyPets {
		val listMyPets = exec(http("ListMyPets")
			.get("/pets/listMyPets"))
		.pause(8)
	}

	object NewVisit {
		val newVisit = exec(http("InitNewVisitForm")
			.get("/pets/newVisit/3")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(22)
		.exec(http("NewVisitCreated")
			.post("/visits/save")
			.headers(headers_2)
			.formParam("description", "Nueva")
			.formParam("dateTime", "2022/07/24 12:30")
			.formParam("id", "")
			.formParam("clinic", "3")
			.formParam("pet", "3")
			.formParam("authorized", "")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	val scnHU014_CreateNewVisit = scenario("scnHU014_CreateNewVisit").exec(
		Home.home,
		Login.login,
		ListMyPets.listMyPets,
		NewVisit.newVisit
	)

	setUp(
		scnHU014_CreateNewVisit.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}