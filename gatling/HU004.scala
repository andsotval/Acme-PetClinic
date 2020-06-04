package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU004 extends Simulation {

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
			.pause(5)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "admin")
			.formParam("password", "admin")
			.formParam("_csrf", "${stoken}"))
			.pause(8)
	}
	
	object SuggestionList {
		val suggestionList = exec(http("SuggestionList")
			.get("/suggestion/admin/list"))
			.pause(13)
	}

	object SuggestionRead {
		val suggestionRead = exec(http("SuggestionRead")
			.get("/suggestion/admin/read/4"))
			.pause(13)
	}
	
	object SuggestionNotRead {
		val suggestionNotRead = exec(http("SuggestionNotRead")
			.get("/suggestion/admin/notRead/1"))
			.pause(7)
	}
	
	val scnHU004_SuggestionRead = scenario("scnHU004_SuggestionRead").exec(
		Home.home,
		Login.login,
		SuggestionList.suggestionList,
		SuggestionRead.suggestionRead
	)
	
	val scnHU004_SuggestionNotRead = scenario("scnHU004_SuggestionNotRead").exec(
		Home.home,
		Login.login,
		SuggestionList.suggestionList,
		SuggestionNotRead.suggestionNotRead
	)

	setUp(
		scnHU004_SuggestionRead.inject(rampUsers(2500) during (100 seconds)),
		scnHU004_SuggestionNotRead.inject(rampUsers(2500) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}