package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU010 extends Simulation {

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
		.pause(55)
	}
	
	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2))
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(16)
		.exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "manager1")
			.formParam("password", "manager1")
			.formParam("_csrf", "${stoken}"))
		.pause(12)
	}
	
	object OrderList {
		val orderList = exec(http("OrderList")
			.get("/orders/list")
			.headers(headers_0))
		.pause(25)
	}
	
	object ShowOrder {
		val showOrder = exec(http("ShowOrder")
			.get("/orders/1")
			.headers(headers_0))
		.pause(22)
	}
	
	val scnHU010_ShowOrder = scenario("ManascnHU010_ShowOrdergers").exec(Home.home,
									  Login.login,
									  OrderList.orderList,
									  ShowOrder.showOrder)	

	setUp(scnHU010_ShowOrder.inject(rampUsers(5000) during (100 seconds))).protocols(httpProtocol).assertions(
        global.responseTime.max.lt(5000),    
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
     )
}