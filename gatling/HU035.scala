package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU035 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.petclinic.com")


	object Home{
		val home = exec(http("Home")
			.get("/"))
		.pause(1)
	}

	object Login{
		val login = exec(http("Login")
			.get("/login"))
		.pause(7)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "provider1")
			.formParam("password", "provider1")
			.formParam("_csrf", "91882580-cb60-4f59-a13a-763e10cd2e02"))
		.pause(7)
		// Login
	}

	object ShowOrders{
		val showOrders = exec(http("ShowOrders")
			.get("/orders/listByProvider"))
		.pause(15)
	}

	object AcceptOrder{
		val acceptOrder = exec(http("AcceptOrder")
			.get("/orders/acceptOrder/4"))
		.pause(10)
	}


	val scnHU035_AcceptOrder = scenario("HU035").exec(
		Home.home,
		Login.login,
		ShowOrders.showOrders,
		AcceptOrder.acceptOrder
	)


	setUp(
		scnHU035_AcceptOrder.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	)

}