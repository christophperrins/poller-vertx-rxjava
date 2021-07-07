package digital.vix.vertxdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import digital.vix.vertxdemo.dto.EndpointHistoryDto;
import digital.vix.vertxdemo.models.EndpointHistory;
import digital.vix.vertxdemo.service.EndpointHistoryService;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import org.modelmapper.ModelMapper;

public class EndpointHistoryController extends AbstractVerticle {

	private Router router;
	private ObjectMapper mapper;
	private EndpointHistoryService endpointHistoryService;
	private ModelMapper modelMapper;

	public EndpointHistoryController(Router router, ObjectMapper mapper, EndpointHistoryService endpointHistoryService, ModelMapper modelMapper) {
		this.router = router;
		this.mapper = mapper;
		this.endpointHistoryService = endpointHistoryService;
		this.modelMapper = modelMapper;
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
				.map(jsonObject -> mapper.readValue(jsonObject.toString(), EndpointHistory.class)).map(endpointHistory -> modelMapper.map(endpointHistory, EndpointHistoryDto.class)).toList()
				.subscribe(endpoints -> routingContext.response().putHeader("Content-Type", "application/json")
						.end(mapper.writeValueAsString(endpoints)));
	}
}
