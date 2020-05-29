package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU032 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Accept-Encoding" -> "gzip, deflate",
		"Accept-Language" -> "es-ES,es;q=0.9",
		"Origin" -> "http://www.petclinic.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_5 = Map(
		"Accept" -> "*/*",
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-CryptoAPI/10.0")

	
	
		object Home {
			val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(9)
		}
		
		object Login {
			val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(13)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}"))
		.pause(13)
		}
		
		object ListSpecialties {
			val listSpecialties = exec(http("ListSpecialties")
			.get("/specialty/admin/listAvailable")
			.headers(headers_0))
		.pause(19)
		}
		
		object NewSpecialty {
			val newSpecialty = exec(http("NewSpecialty")
			.get("/specialty/admin/new")
			.headers(headers_0))
		.pause(28)
		}
		
		object CreateNewSpecialty {
			val createNewSpecialty = exec(http("CreateNewSpecialty")
			.get("/specialty/admin/new")
			.headers(headers_0))
		.pause(28)
		}
		
	val scnHU032_CreateNewSpecialty = scenario("scnHU032_CreateNewSpecialty").exec(Home.home,
									  Login.login,
									  ListSpecialties.listSpecialties,
									  NewSpecialty.newSpecialty,
									  CreateNewSpecialty.createNewSpecialty)

	setUp(scnHU032_CreateNewSpecialty.inject(rampUsers(8000) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}