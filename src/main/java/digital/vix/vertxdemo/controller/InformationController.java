package digital.vix.vertxdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.vix.vertxdemo.models.Information;
import digital.vix.vertxdemo.service.InformationService;
import io.reactivex.Completable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

public class InformationController extends AbstractVerticle {

	private Router router;
	private ObjectMapper mapper;
	private InformationService informationService;

	public InformationController(Router router, ObjectMapper mapper, InformationService informationService) {
		this.router = router;
		this.mapper = mapper;
		this.informationService = informationService;
	}

	@Override
	public Completable rxStart() {
		router.get("/api/information").handler(this::get);
		router.post("/api/information").handler(BodyHandler.create()).handler(this::add);
		router.put("/api/information").handler(BodyHandler.create()).handler(this::update);
		router.delete("/api/information/:id").handler(this::delete);
		return informationService.all().map(jsonObject -> {
			Information information = mapper.readValue(jsonObject.toString(), Information.class);
			return information;
		}).ignoreElements();
	}

	public void get(RoutingContext routingContext) {
		informationService.all().map(jsonObject -> {
			return mapper.readValue(jsonObject.toString(), Information.class);
		}).toList().subscribe(informations -> routingContext.response().putHeader("Content-Type", "application/json")
				.end(mapper.writeValueAsString(informations)));
	}

	public void add(RoutingContext routingContext) {
		try {
			Information information = mapper.readValue(routingContext.getBodyAsString(), Information.class);
			informationService.create(information).subscribe(id -> {
				information.setId(id);
				routingContext.response().end(String.valueOf(id));
			});
		} catch (JsonProcessingException e) {
			routingContext.response().setStatusCode(400).end("Body was not in correct JSON format");
		}
	}

	public void update(RoutingContext routingContext) {
		Information information = null;
		try {
			information = mapper.readValue(routingContext.getBodyAsString(), Information.class);
		} catch (JsonProcessingException e) {
			routingContext.response().setStatusCode(400).end("Body was not in correct JSON format");
			return;
		}

		if (!information.hasFieldsForUpdate()) {
			System.out.println(information);
			routingContext.response().setStatusCode(400).end("Body not in correct format");
			return;
		}

		informationService.update(information).subscribe(() -> routingContext.response().end());
	}

	public void delete(RoutingContext routingContext) {
		long id = -1;
		try {
			String idText = routingContext.request().getParam("id");
			id = Long.parseLong(idText);
		} catch (Exception e) {
			routingContext.response().setStatusCode(400).end("Id not valid");
		}

		informationService.delete(id).subscribe(() -> routingContext.response().end());
	}

}
