package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU007 extends Simulation {

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
		.pause(15)
	}
	
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))	
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(18)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "owner1")
			.formParam("_csrf", "${stoken}"))
		.pause(15)
	}
	
	
	object ShowSuggestionList{
		val showSuggestionList = exec(http("ShowSuggestionList")
			.get("/suggestion/user/list")
			.headers(headers_0))
		.pause(30)
	}
	
	object NewSuggestionForm{
		val newSuggestionForm = exec(http("NewSuggestionForm")
			.get("/suggestion/user/new")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(39)
		.exec(http("SaveNewSuggestion")
			.post("/suggestion/user/save")
			.headers(headers_3)
			.formParam("created", "2020/05/25 12:53:00")
			.formParam("isRead", "false")
			.formParam("isTrash", "false")
			.formParam("isAvailable", "true")
			.formParam("user", "26")
			.formParam("name", "Nueva sugerencia")
			.formParam("description", "Tengo una nueva sugerencia")
			.formParam("_csrf", "${stoken}"))
		.pause(22)
	}	
	

	val scnHU007_CreateNewSuggestion = scenario("scnHU007_CreateNewSuggestion").exec(Home.home,
									  Login.login,
									  ShowSuggestionList.showSuggestionList,
									  NewSuggestionForm.newSuggestionForm)
									  
									  
		

	setUp(scnHU007_CreateNewSuggestion.inject(rampUsers(5000) during (100 seconds))).protocols(httpProtocol)
	.assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
	 
}