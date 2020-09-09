package digital.vix.vertxdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.service.EndpointHistoryService;
import digital.vix.vertxdemo.service.EndpointService;
import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class EndpointController extends AbstractVerticle {

	private Router router;
	private ObjectMapper mapper;
	private EndpointService endpointService;
	private EndpointHistoryService endpointHistoryService;

	public EndpointController(Router router, ObjectMapper mapper, EndpointService endpointService,
			EndpointHistoryService endpointHistoryService) {
		this.router = router;
		this.mapper = mapper;
		this.endpointService = endpointService;
		this.endpointHistoryService = endpointHistoryService;
	}

	@Override
	public Completable rxStart() {
		router.get("/api/poll").handler(this::get);
		router.post("/api/poll").handler(BodyHandler.create()).handler(this::add);
		router.put("/api/poll").handler(BodyHandler.create()).handler(this::update);
		router.delete("/api/poll/:id").handler(this::delete);
		return endpointService.readAllEndpoints().map(jsonObject -> {
			Endpoint endpoint = mapper.readValue(jsonObject.toString(), Endpoint.class);
			endpointHistoryService.pollEndpoint(endpoint);
			return endpoint;
		}).ignoreElements();
	}

	public void get(RoutingContext routingContext) {
		endpointService.readAllEndpoints().map(jsonObject -> {
			return mapper.readValue(jsonObject.toString(), Endpoint.class);
		}).toList().subscribe(endpoints -> routingContext.response().putHeader("Content-Type", "application/json")
				.end(mapper.writeValueAsString(endpoints)));
	}

	public void add(RoutingContext routingContext) {
		try {
			Endpoint endpoint = mapper.readValue(routingContext.getBodyAsString(), Endpoint.class);
			endpoint.setActive(true);
			endpointService.addEndpoint(endpoint).subscribe(id -> {
				endpoint.setId(id);
				endpointHistoryService.pollEndpoint(endpoint);
				routingContext.response().end(String.valueOf(id));
			});
		} catch (JsonProcessingException e) {
			routingContext.response().setStatusCode(400).end("Body was not in correct JSON format");
		}
	}

	public void update(RoutingContext routingContext) {
		Endpoint endpoint = null;
		try {
			endpoint = mapper.readValue(routingContext.getBodyAsString(), Endpoint.class);
		} catch (JsonProcessingException e) {
			routingContext.response().setStatusCode(400).end("Body was not in correct JSON format");
			return;
		}

		if (!endpoint.hasFieldsForUpdate()) {
			System.out.println(endpoint);
			routingContext.response().setStatusCode(400).end("Body not in correct format");
			return;
		}
		endpointHistoryService.pollEndpoint(endpoint);

		endpointService.updateEndpoint(endpoint).subscribe(() -> routingContext.response().end());
	}

	public void delete(RoutingContext routingContext) {
		long id = -1;
		try {
			String idText = routingContext.request().getParam("id");
			id = Long.parseLong(idText);
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end("Id not valid");
		}
		endpointHistoryService.stopPolling(id);

		endpointService.deleteEndpoint(id).subscribe(() -> routingContext.response().end());
	}

}
