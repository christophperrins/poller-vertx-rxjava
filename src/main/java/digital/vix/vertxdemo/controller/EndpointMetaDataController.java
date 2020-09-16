package digital.vix.vertxdemo.controller;

import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import digital.vix.vertxdemo.dto.EndpointMetaDataDto;
import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.models.Information;
import digital.vix.vertxdemo.service.EndpointService;
import digital.vix.vertxdemo.service.InformationService;
import digital.vix.vertxdemo.utils.ArrayUtils;
import io.reactivex.Flowable;
import io.reactivex.Single;
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
        this.router = router;
        this.mapper = mapper;
        this.endpointService = endpointService;
        this.informationService = informationService;
    }

    @Override
    public void start() {
        router.get("/api/endpoint").handler(this::getEndpoint);
    }

    /**
     * Method to determine whether multiple ids have been entered or a single id.
     * <br>
     * <br>
     * <p>
     * If using a single id the following should be used /api/endpoint?id=3 <br>
     * Calls: {@link #getEndpointById(RoutingContext)} <br>
     * <br>
     * <p>
     * Whereas when using multiple ids: /api/endpoint?ids=3%2C4$2C5 (which
     * translates to 3,4,5)<br>
     * Calls: {@link #getEndpointsByIds(RoutingContext)}<br>
     *
     * @param routingContext
     */
    public void getEndpoint(RoutingContext routingContext) {
        if (routingContext.request().getParam("id") != null) {
            getEndpointById(routingContext);
        } else if (routingContext.request().getParam("ids") != null) {
            getEndpointsByIds(routingContext);
        }
    }

    public void getEndpointById(RoutingContext routingContext) {
        try {
            String idText = routingContext.request().getParam("id");
            long id = Long.parseLong(idText);

            Flowable<Endpoint> endpoints = endpointService.readAllEndpoints().map(jsonObject -> {
                return mapper.readValue(jsonObject.toString(), Endpoint.class);
            });

            Single<Endpoint> foundEndpoint = filterEndpointsById(endpoints, id);

            Single<EndpointMetaDataDto> endpointAndMetaData =
                    foundEndpoint.flatMap(endpoint -> attachInformationToEndpoint(endpoint));

            endpointAndMetaData.subscribe(endpointMeta ->
                    routingContext.response().end(mapper.writeValueAsString(endpointMeta)));

        } catch (NumberFormatException e) {
            routingContext.response().setStatusCode(400).end("Id not valid");
        }
    }

    private Single<Endpoint> filterEndpointsById(Flowable<Endpoint> endpoints, long id) {
        return endpoints.filter(endpoint -> endpoint.getId() == id).singleOrError();
    }

    private Single<EndpointMetaDataDto> attachInformationToEndpoint(Endpoint endpoint) {
        return informationService.findByEndPointId(endpoint.getId()).map(rs -> rs.getRows().get(0))
                .map(json -> mapper.readValue(json.toString(), Information.class))
                .map(information -> new EndpointMetaDataDto(endpoint, information));
    }

    public void getEndpointsByIds(RoutingContext routingContext) {
        try {
            String[] idTexts = routingContext.request().getParam("ids").split(",");
            final long[] ids = new long[idTexts.length];
            for (int i = 0; i < idTexts.length; i++) {
                ids[i] = Long.parseLong(idTexts[i]);
            }
            endpointService.readAllEndpoints().map(jsonObject -> {
                return mapper.readValue(jsonObject.toString(), Endpoint.class);
            }).filter(endpoint -> ArrayUtils.arrayContains(ids, endpoint.getId())).toList().flatMap(endpointList -> {
                return informationService.findByEndPointIds(ids).map(json -> mapper.readValue(json.toString(), Information.class)).toList().map(informationList -> {
                    return endpointList.stream().map(endpoint -> {
                        Information foundInfo = informationList.stream()
                                .filter(information -> information.getEndPointId() == endpoint.getId()).findFirst()
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
