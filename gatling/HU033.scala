package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU033 extends Simulation {

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
		"Origin" -> "http://www.petclinic.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")


	object Home {
			val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(10)
	}
	object Login {
			val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(8)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "vet1")
			.formParam("password", "vet1")
			.formParam("_csrf", "${stoken}"))
		.pause(24)
	}
	
	object ListSpecialty {
			val listSpecialty = exec(http("ListSpecialty")
			.get("/specialty/vet/list")
			.headers(headers_0))
		.pause(49)
	}
	
	object AddSpecialty {
			val addSpecialty = exec(http("AddSpecialty")
			.get("/specialty/vet/add/4")
			.headers(headers_0))
		.pause(63)
	}
	
	
	val vetScn = scenario("Vets").exec(Home.home,
									  Login.login,
									  ListSpecialty.ListSpecialty,
									  AddSpecialty.AddSpecialty);
	

	setUp(vetScn.inject(atOnceUsers(1))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}