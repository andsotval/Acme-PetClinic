package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU001 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_2 = Map("Origin" -> "http://www.petclinic.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
			.pause(7)
	}	
	
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(10)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}"))
			.pause(12)
	}
	
	object PetTypeList {
		val petTypeList = exec(http("PetTypeList")
			.get("/pettype/listAvailable"))
			.pause(10)
	}
	
	object PetTypeCreated {
		val petTypeCreated = exec(http("request_4")
			.get("/pettype/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(5)
		.exec(http("PetTypeCreated")
			.post("/pettype/new")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("available", "true")
			.formParam("name", "raton")
			.formParam("_csrf", "${stoken}"))
		.pause(7)
	}

	val scnHU001_PetTypeCreated = scenario("scnHU001_PetTypeCreated").exec(
		Home.home,
		Login.login,
		PetTypeList.petTypeList,
		PetTypeCreated.petTypeCreated
	)

	setUp(
		scnHU001_PetTypeCreated.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}