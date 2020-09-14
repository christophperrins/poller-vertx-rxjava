package digital.vix.vertxdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import digital.vix.vertxdemo.dto.EndpointMetaDataDto;
import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.models.Information;
import digital.vix.vertxdemo.service.EndpointService;
import digital.vix.vertxdemo.service.InformationService;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;

public class EndpointMetaDataController extends AbstractVerticle {

	private Router router;
	private ObjectMapper mapper;
	private EndpointService endpointService;
	private InformationService informationService;

	public EndpointMetaDataController(Router router, ObjectMapper mapper, EndpointService endpointService,
			InformationService informationService) {
		super();
		this.router = router;
		this.mapper = mapper;
		this.endpointService = endpointService;
		this.informationService = informationService;
	}

	@Override
	public void start() {
		router.get("/api/endpoint").handler(this::getEndpoint);
	}

	public void getEndpoint(RoutingContext routingContext) {
		if (routingContext.request().getParam("id") != null) {
			getEndpointById(routingContext);
		} else if (routingContext.request().getParam("ids") != null) {
			getEndpointsByIds(routingContext);
		}
	}

	public void getEndpointById(RoutingContext routingContext) {
		System.out.println("ID");
		try {
			String idText = routingContext.request().getParam("id");
			long id = Long.parseLong(idText);
			endpointService.readAllEndpoints().map(jsonObject -> {
				return mapper.readValue(jsonObject.toString(), Endpoint.class);
			}).filter(endpoint -> endpoint.getId() == id).singleOrError().flatMap(endpoint -> {
				return informationService.findEndpointById(id).map(information -> {
					return new EndpointMetaDataDto(endpoint, information);
				});
			}).subscribe(endpointMeta -> {
				routingContext.response().end(mapper.writeValueAsString(endpointMeta));
			});
		} catch (NumberFormatException e) {
			routingContext.response().setStatusCode(400).end("Id not valid");
		}

	}

	private boolean contains(long search, long[] values) {
		for (long value : values) {
			if (search == value)
				return true;
		}
		return false;
	}

	public void getEndpointsByIds(RoutingContext routingContext) {
		System.out.println("IDS");
		try {
			String[] idTexts = routingContext.request().getParam("ids").split(",");
			final long[] ids = new long[idTexts.length];
			for (int i = 0; i < idTexts.length; i++) {
				ids[i] = Long.parseLong(idTexts[i]);
			}
			endpointService.readAllEndpoints().map(jsonObject -> {
				return mapper.readValue(jsonObject.toString(), Endpoint.class);
			}).filter(endpoint -> contains(endpoint.getId(), ids)).toList().flatMap(endpointList -> {
				return informationService.findEndpointsByIds(ids).toList().map(informationList -> {

					return endpointList.stream().map(endpoint -> {
						Information foundInfo = informationList.stream()
								.filter(information -> information.getEndpointId() == endpoint.getId()).findFirst()
								.orElseGet(() -> new Information());
						return new EndpointMetaDataDto(endpoint, foundInfo);
					}).collect(Collectors.toList());
				});
			}).subscribe(endpointMetas -> {
				routingContext.response().end(mapper.writeValueAsString(endpointMetas));
			});
		} catch (NumberFormatException e) {
			routingContext.response().setStatusCode(400).end("not valid ids");
			return;
		}
	}

}
