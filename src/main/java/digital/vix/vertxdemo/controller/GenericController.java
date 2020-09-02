package digital.vix.vertxdemo.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

public abstract class GenericController extends AbstractVerticle {

	private Router router;

	public GenericController(Router router) {
		super();
		this.router = router;
	}

	public Router getRouter() {
		return router;
	}

	public void setRouter(Router router) {
		this.router = router;
	}
	
	@Override
	public abstract void start();

}
