package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU002 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3",
		"Origin" -> "http://www.petclinic.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_10 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

    val uri2 = "http://tile-service.weather.microsoft.com/es-ES/livetile/preinstall"

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
			.pause(7)
	}
	
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(5)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}"))
			.pause(8)
	}
	
	object PetTypeList {
			val petTypeList = exec(http("PetTypeList")
			.get("/pettype/listAvailable")
			.headers(headers_0))
			.pause(17)
	}
	
	object PetTypeDisable {
			val petTypeDisable = exec(http("PetTypeDisable")
			.get("/pettype/notAvailable/1")
			.headers(headers_0))
			.pause(13)
	}
	
	object PetTypeAvailable {
			val petTypeAvailable = exec(http("PetTypeAvailable")
			.get("/pettype/available/7")
			.headers(headers_0))
			.pause(23)
	}
	
	object PetTypeUpdate {
			val petTypeUpdate = exec(http("request_8")
			.get("/pettype/edit/2")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
			.pause(5)
		.exec(http("request_9")
			.post("/pettype/edit/2")
			.headers(headers_2)
			.formParam("id", "2")
			.formParam("available", "true")
			.formParam("name", "dogggg")
			.formParam("_csrf", "${stoken}"))
			.pause(1)
	}
	
	val scnHU002_PetTypeDisable = scenario("scnHU002_PetTypeDisable").exec(
		Home.home,
		Login.login,
		PetTypeList.petTypeList,
		PetTypeDisable.petTypeDisable
	)
	
	val scnHU002_PetTypeAvailable = scenario("scnHU002_PetTypeAvailable").exec(
		Home.home,
		Login.login,
		PetTypeList.petTypeList,
		PetTypeAvailable.petTypeAvailable
	)
	
	val scnHU002_PetTypeUpdate = scenario("scnHU002_PetTypeUpdate").exec(
		Home.home,
		Login.login,
		PetTypeList.petTypeList,
		PetTypeUpdate.petTypeUpdate
	)


	setUp(
		scnHU002_PetTypeDisable.inject(rampUsers(1666) during (100 seconds)),
		scnHU002_PetTypeAvailable.inject(rampUsers(1666) during (100 seconds)),
		scnHU002_PetTypeUpdate.inject(rampUsers(1666) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}