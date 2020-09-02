package digital.vix.vertxdemo.controller;

import digital.vix.vertxdemo.models.Endpoint;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class PollerManagerController extends GenericController {

	public PollerManagerController(Router router) {
		super(router);
	}

	@Override
	public void start() {
		Route messageRoute = getRouter().get("/api/poll"); // <1>
		Endpoint endpoint = new Endpoint(2L, "Hello", "endpoint", 1000);
		messageRoute.handler(rc -> {
			rc.response().end(endpoint.toString()); // <2>
		});
	}
}
