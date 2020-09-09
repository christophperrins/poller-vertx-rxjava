package digital.vix.vertxdemo.service;

import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.models.EndpointHistory;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.vertx.core.json.JsonObject;

public interface EndpointHistoryService {
	
	public Flowable<JsonObject> readHistory(long id);

	public Completable addHistory(EndpointHistory endpointHistory);
	
	public void pollEndpoint(Endpoint Endpoint);
	
	public void stopPolling(long id);
}
