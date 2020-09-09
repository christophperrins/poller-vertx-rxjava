package digital.vix.vertxdemo.repository;

import java.util.List;

import digital.vix.vertxdemo.models.Endpoint;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.sql.SQLClient;

public class EndpointRepositoryImpl implements EndpointRepository {

	private SQLClient sqlClient;

	public EndpointRepositoryImpl(SQLClient sqlClient) {
		this.sqlClient = sqlClient;
	}

	@Override
	public Flowable<JsonObject> readAllEndpoints() {
		return sqlClient.rxQuery("SELECT * FROM endpoints where active = true")
				.flatMapPublisher(resultSet -> Flowable.fromIterable(resultSet.getRows()));
	}

	public Single<Long> findEndpoint(String endpoint) {
		return sqlClient.rxQueryWithParams("SELECT id FROM endpoints where endpoint = ?", new JsonArray().add(endpoint))
				.map(rs -> {
					List<JsonObject> rows = rs.getRows();
					if (rows.size() == 0) {
						return -1l;
					} else {
						return rows.get(0).getLong("id");
					}
				});
	}
	

	@Override
	public Single<Single<Long>> addEndpoint(Endpoint endpoint) {
		return findEndpoint(endpoint.getEndpoint()).map(exists -> {
			if (exists == -1l) {
				return sqlClient
						.rxUpdateWithParams(
								"INSERT INTO endpoints(name, endpoint, frequency, active) values (?, ?, ?, ?)",
								new JsonArray().add(endpoint.getName()).add(endpoint.getEndpoint())
										.add(endpoint.getFrequency()).add(true))
						.map(updateResult -> updateResult.getKeys().getLong(0));

			} else {
				updateEndpoint(endpoint).subscribe();
				return Single.just(exists);
			}
		});

	}

	@Override
	public Completable updateEndpoint(Endpoint endpoint) {
		return sqlClient.rxUpdateWithParams("UPDATE endpoints set name = ?, frequency = ?, active = ? where endpoint = ?",
				new JsonArray().add(endpoint.getName()).add(endpoint.getFrequency()).add(endpoint.isActive()).add(endpoint.getEndpoint()))
				.ignoreElement();
	}

	@Override
	public Completable deleteEndpoint(long id) {
		return sqlClient.rxUpdateWithParams("UPDATE endpoints set active = false where id = ?", new JsonArray().add(id))
				.ignoreElement();
	}

}
