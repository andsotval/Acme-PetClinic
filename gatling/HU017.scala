package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU017 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_1 = Map("Origin" -> "http://www.petclinic.com")

	object Home {
		val home = exec(http("Home")
			.get("/"))
			.pause(7)
	}	

	object Login{
		val login = exec(http("Login")
			.get("/login")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(16)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "owner1")
			.formParam("password", "owner1")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
	}

	object ShowMyPets{
		val showMyPets = exec(http("ShowMyPets")
			.get("/pets/listMyPets"))
		.pause(14)
	}

	object SaveNewPet{
		val saveNewPet = exec(http("FormNewPet")
			.get("/pets/new")
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(23).exec(http("SaveNewPet")
			.post("/pets/save")
			.headers(headers_1)
			.formParam("id", "")
			.formParam("owner", "1")
			.formParam("name", "Wiskers")
			.formParam("birthDate", "2020/05/07")
			.formParam("type", "cat")
			.formParam("_csrf", "${stoken}"))
		.pause(18)
	}

	val scnHU017_AddNewPet = scenario("scnHU017_SaveNewPet").exec(
		Home.home,
		Login.login,
		ShowMyPets.showMyPets,
		SaveNewPet.saveNewPet
	)

	setUp(
		scnHU017_AddNewPet.inject(rampUsers(10000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	)
}