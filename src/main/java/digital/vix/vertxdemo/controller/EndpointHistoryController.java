package digital.vix.vertxdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import digital.vix.vertxdemo.models.EndpointHistory;
import digital.vix.vertxdemo.service.EndpointHistoryService;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;

public class EndpointHistoryController extends AbstractVerticle {

	private Router router;
	private ObjectMapper mapper;
	private EndpointHistoryService endpointHistoryService;

	public EndpointHistoryController(Router router, ObjectMapper mapper,
			EndpointHistoryService endpointHistoryService) {
		super();
		this.router = router;
		this.mapper = mapper;
		this.endpointHistoryService = endpointHistoryService;
	}

	@Override
	public void start() {
		router.get("/api/history/:id").handler(this::get);
	}
	
	public void get(RoutingContext routingContext) {
		long id = -1;
		try {
			String idText = routingContext.request().getParam("id");
			id = Long.parseLong(idText);
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end("Id not valid");
		}

		endpointHistoryService.readHistory(id)
				.map(jsonObject -> mapper.readValue(jsonObject.toString(), EndpointHistory.class)).toList()
				.subscribe(endpoints -> routingContext.response().putHeader("Content-Type", "application/json")
						.end(mapper.writeValueAsString(endpoints)));
	}
}
