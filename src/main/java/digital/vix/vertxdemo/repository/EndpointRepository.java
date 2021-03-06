package digital.vix.vertxdemo.repository;

import digital.vix.vertxdemo.models.Endpoint;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;

public interface EndpointRepository {

	public Flowable<JsonObject> readAllEndpoints();

	public Flowable<JsonObject> findByEndPointIds(long[] id);
	
	public Single<Long> findOrAddEndpointByHostname(Endpoint endpoint);

	public Completable updateEndpoint(Endpoint endpoint);
	
	public Completable deleteEndpoint(long id);
	
}
