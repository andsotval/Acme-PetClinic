package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU008 extends Simulation {

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
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(12)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "manager1")
			.formParam("password", "manager1")
			.formParam("_csrf", "${stoken}"))
		.pause(19)
	}
	
	object ListOrders {
		val listOrders = exec(http("ListOrders")
			.get("/orders/list")
			.headers(headers_0))
		.pause(20)
	}
	
	object ListAvailableProviders {
		val listAvailableProviders = exec(http("ListAvailableProviders")
			.get("/orders/providers/listAvailable")
			.headers(headers_0))
		.pause(20)
	}
	
	object NewOrder {
		val newOrder = exec(http("NewOrder")
			.get("/orders/new/1")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(33)
		.exec(http("SaveNewOrder")
			.post("/orders/save/1")
			.headers(headers_3)
			.formParam("productIds", "4")
			.formParam("amountNumber", "3")
			.formParam("productIds", "5")
			.formParam("amountNumber", "0")
			.formParam("productIds", "7")
			.formParam("amountNumber", "0")
			.formParam("_csrf", "${stoken}"))
		.pause(23)
	}

	val scnHU008_CreateNewOrder = scenario("scnHU008_CreateNewOrder").exec(Home.home,
									  Login.login,
									  ListOrders.listOrders,
									  ListAvailableProviders.listAvailableProviders,
									  NewOrder.newOrder)
	

	setUp(scnHU008_CreateNewOrder.inject(rampUsers(5000) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}