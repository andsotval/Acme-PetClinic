package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU018 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_1 = Map("Origin" -> "http://www.petclinic.com")


	object Home{
		val home = exec(http("Home")
			.get("/login"))
		.pause(16)
	}

	object Login{
		val login = exec(http("Login")
			.post("/login")
			.headers(headers_1)
			.formParam("username", "owner1")
			.formParam("password", "owner1")
			.formParam("_csrf", "54c68dac-7aff-4f3f-bc3d-5e82787917e7"))
		.pause(13)
	}

	object ShowMyPets{
		val ShowMyPets = exec(http("ShowMyPets")
			.get("/pets/listMyPets"))
		.pause(14)
	}

	object FormNewPet{
		val formNewPet = exec(http("FormNewPet")
			.get("/pets/new"))
		.pause(23)
	}

	object SaveNewPet{
		val saveNewPet = exec(http("SaveNewPet")
			.post("/pets/save")
			.headers(headers_1)
			.formParam("id", "")
			.formParam("owner", "1")
			.formParam("name", "Wiskers")
			.formParam("birthDate", "2020/05/07")
			.formParam("type", "cat")
			.formParam("_csrf", "4ceca828-8017-44a3-8ac2-1b2a8178dcba"))
		.pause(18)
	}

	object DeletePet{
		val deletePet = exec(http("DeletePet")
			.get("/pets/delete/15"))
		.pause(7)
	}

	val scnHU011_AddNewPet = scenario("scnHU011_AddNewPet").exec(
		Home.home,
		Login.login,
		ShowMyPets.ShowMyPets,
		FormNewPet.formNewPet,
		SaveNewPet.saveNewPet
	)
	val scnHU012_deletePet = scenario("scnHU012_DeletePet").exec(
		Home.home,
		Login.login,
		ShowMyPets.ShowMyPets,
		FormNewPet.formNewPet,
		SaveNewPet.saveNewPet,
		DeletePet.deletePet
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