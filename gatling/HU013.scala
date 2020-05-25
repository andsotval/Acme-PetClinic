package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU013 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_0 = Map("Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Origin" -> "http://www.petclinic.com",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_7 = Map("Accept" -> "image/webp,*/*")

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
			.formParam("username", "owner1")
			.formParam("password", "owner1")
			.formParam("_csrf", "${stoken}"))
			.pause(12)
	}

	object PetList {
		val petList = exec(http("ListMyPets")
			.get("/pets/listMyPets")
			.headers(headers_0))
		.pause(11)
	}
	
	object CreateStay {
		val createStay = exec(http("CreateStay")
			.get("/pets/newStay/1")
			.headers(headers_0))
		.pause(23)
	}
	
	object CreateCorrectStay {
		val createCorrectStay = exec(http("CreateCorrectStay")
			.post("/stays/save")
			.headers(headers_2)
			.formParam("description", "Description")
			.formParam("startDate", "2020/05/31")
			.formParam("finishDate", "2020/06/06")
			.formParam("id", "")
			.formParam("clinic", "1")
			.formParam("pet", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(9)
	}

	val scnHU013_StayCreate = scenario("HU013")
		.exec(Home.home,
		Login.login,
		PetList.petList,
		CreateStay.createStay,
		CreateCorrectStay.createCorrectStay)


	setUp(scnHU013_StayCreate.inject(rampUsers(5000) during (100 seconds)))
	.protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}