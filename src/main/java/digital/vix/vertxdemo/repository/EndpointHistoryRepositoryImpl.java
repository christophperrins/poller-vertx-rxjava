package digital.vix.vertxdemo.repository;

import java.text.SimpleDateFormat;

import digital.vix.vertxdemo.models.EndpointHistory;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.sql.SQLClient;

public class EndpointHistoryRepositoryImpl implements EndpointHistoryRepository {

	private SQLClient sqlClient;

	public EndpointHistoryRepositoryImpl(SQLClient sqlClient) {
		this.sqlClient = sqlClient;
	}

	@Override
	public Flowable<JsonObject> readHistory(long id) {
		return sqlClient
				.rxQueryWithParams("SELECT * FROM endpoint_history where endpoint_id = ?", new JsonArray().add(id))
				.flatMapPublisher(resultSet -> Flowable.fromIterable(resultSet.getRows()));
	}

	@Override
	public Completable addHistory(EndpointHistory endpointHistory) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String date = sdf.format(endpointHistory.getTimedate());

		return sqlClient.rxUpdateWithParams("INSERT INTO endpoint_history(endpoint_id, status, response_time, timedate) VALUES(?, ?, ?, ?)",
				new JsonArray().add(endpointHistory.getEndpointId()).add(endpointHistory.getStatus())
						.add(endpointHistory.getResponseTime()).add(date))
				.ignoreElement();
	}

}
