package digital.vix.vertxdemo;

import digital.vix.vertxdemo.controller.PollerManagerController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class Application extends AbstractVerticle {

	public static void main(String[] args) {
		Vertx vertx = Vertx.factory.vertx();
		Router router = Router.router(vertx);
		vertx.deployVerticle(new PollerManagerController(router));

		vertx.createHttpServer().requestHandler(router).listen(8080);
	}
}
