package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU027 extends Simulation {

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

	val headers_8 = Map("Accept" -> "image/webp,*/*")

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
			.formParam("username", "vet1")
			.formParam("password", "vet1")
			.formParam("_csrf", "${stoken}"))
			.pause(12)
	}
		
	object ListAcceptStays {
		val listAcceptStays = exec(http("ListAcceptStays")
			.get("/stays/listAllAccepted"))
		.pause(22)
	}

	object ModifyStay {
		val modifyStay = exec(http("ModifyStay")
			.get("/stays/changeDate/1")
			.headers(headers_0))
		.pause(22)
	}
	
	object ModifyCorrectStay {
		val modifyCorrectStay = exec(http("ModifyCorrectStay")
			.post("/stays/save/1")
			.headers(headers_2)
			.formParam("description", "stay modificada")
			.formParam("startDate", "2020/07/09")
			.formParam("finishDate", "2020/07/12")
			.formParam("id", "1")
			.formParam("_csrf", "${stoken}"))
		.pause(50)
	}

	val scnHU027_ModifyStay = scenario("HU027")
				.exec(Home.home,
		Login.login,
		ListAcceptStays.listAcceptStays,
		ModifyStay.modifyStay,
		ModifyCorrectStay.modifyCorrectStay)


	setUp(scnHU027_ModifyStay.inject(rampUsers(5000) during (100 seconds)))
	.protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}