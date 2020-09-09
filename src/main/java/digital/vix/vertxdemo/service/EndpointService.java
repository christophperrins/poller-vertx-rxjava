package digital.vix.vertxdemo.service;

import digital.vix.vertxdemo.models.Endpoint;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

public interface EndpointService {

	public Flowable<JsonObject> readAllEndpoints();
		
	public Single<Long> addEndpoint(Endpoint endpoint);
	
	public Completable updateEndpoint(Endpoint endpoint);
	
	public Completable deleteEndpoint(long id);
}
