package digital.vix.vertxdemo.service;

import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.repository.EndpointRepository;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

public class EndpointServiceImpl implements EndpointService {

	private EndpointRepository endpointRepository;

	public EndpointServiceImpl(EndpointRepository endpointRepository) {
		this.endpointRepository = endpointRepository;
	}

	@Override
	public Flowable<JsonObject> readAllEndpoints() {
		return endpointRepository.readAllEndpoints();
	}

	@Override
	public Single<Long> addEndpoint(Endpoint endpoint) {
		return endpointRepository.findOrAddEndpointByHostname(endpoint);
	}

	@Override
	public Completable updateEndpoint(Endpoint endpoint) {
		return endpointRepository.updateEndpoint(endpoint);
	}

	@Override
	public Completable deleteEndpoint(long id) {
		return endpointRepository.deleteEndpoint(id);
	}

}
