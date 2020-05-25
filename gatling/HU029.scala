package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU029 extends Simulation {

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

	val headers_5 = Map("Accept" -> "image/webp,*/*")

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
	
	object DetailMyClinic {
		val detailMyClinic = exec(http("DetailMyClinic")
			.get("/clinics/getDetail"))
		.pause(13)
	}

	val scnHU029_DetailClinic = scenario("HU029")
		.exec(Home.home,
		Login.login,
		DetailMyClinic.detailMyClinic)

	setUp(scnHU029_DetailClinic.inject(rampUsers(5000) during (100 seconds)))
	.protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}