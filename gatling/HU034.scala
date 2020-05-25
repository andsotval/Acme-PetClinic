package petclinic

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HU034 extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.petclinic.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.8,en-US;q=0.5,en;q=0.3")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:76.0) Gecko/20100101 Firefox/76.0")

	val headers_2 = Map("Origin" -> "http://www.petclinic.com")

	object Home{
		var home = exec(http("Home")
			.get("/"))
		.pause(10)
	}
	object Login{
		var login = exec(http("Login")
			.get("/login"))
		.pause(12)
		.exec(http("Login_2")
			.post("/login")
			.headers(headers_2)
			.formParam("username", "provider1")
			.formParam("password", "provider1")
			.formParam("_csrf", "cd9d0672-37a4-4004-971c-5eef404e0cee"))
		.pause(18)
	}
	object ShowProducts{
		var  showProducts= exec(http("ShowProducts")
			.get("/product/myProductsList"))
		.pause(42)
	}
	object ActivateProduct{
		var activateProduct = exec(http("ActivateProduct")
			.get("/product/desactivateProduct/6"))
		.pause(25)
	}
	object ShowProduct{
		var  showProduct= exec(http("ShowProduct")
			.get("/product/show/4"))
		.pause(14)
	}
	object UpdateProduct{
		var updateProduct = exec(http("UpdateProduct")
			.post("/product/save/")
			.headers(headers_2)
			.formParam("id", "4")
			.formParam("provider", "1")
			.formParam("available", "true")
			.formParam("name", "Bisturi2")
			.formParam("price", "50.99")
			.formParam("tax", "9.79")
			.formParam("_csrf", "4c29ef53-1618-4ae3-820a-a107cdf70ed8"))
		.pause(14)
	}
	object NewProductForm{
		var newProductForm = exec(http("NewProductForm")
			.get("/product/initNewProduct"))
		.pause(28)
	}
	object SaveNewProduct{
		var saveNewProduct = exec(http("SaveNewProduct")
			.post("/product/save")
			.headers(headers_2)
			.formParam("id", "")
			.formParam("provider", "1")
			.formParam("available", "true")
			.formParam("name", "Gasas")
			.formParam("price", "15.4")
			.formParam("tax", "1.1")
			.formParam("_csrf", "4c29ef53-1618-4ae3-820a-a107cdf70ed8"))
		.pause(9)
	}


	val scnHU034_providerManageProducts = scenario("HU034_providerManageProducts").exec(
		Home.home,
		Login.login,
		ShowProducts.showProducts,
		ActivateProduct.activateProduct,
		ShowProduct.showProduct,
		UpdateProduct.updateProduct,
		NewProductForm.newProductForm,
		SaveNewProduct.saveNewProduct
	)

	setUp(
		scnHU034_providerManageProducts.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        global.responseTime.max.lt(5000),
        global.responseTime.mean.lt(1000),
        global.successfulRequests.percent.gt(95)
	)
}