package digital.vix.vertxdemo.repository;

import digital.vix.vertxdemo.models.EndpointHistory;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.vertx.core.json.JsonObject;

public interface EndpointHistoryRepository {

	public Flowable<JsonObject> readHistory(long id);

	public Completable addHistory(EndpointHistory endpointHistory);
}
