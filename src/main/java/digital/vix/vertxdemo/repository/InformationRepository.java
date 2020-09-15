package digital.vix.vertxdemo.repository;

import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.models.Information;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;

public interface InformationRepository {
    public Flowable<JsonObject> all();

    public Single<ResultSet> findByEndPointId(long id);

    public Single<ResultSet> findById(long id);

    public Flowable<JsonObject> findByEndPointIds(long[] ids);

    public Completable update(Information information);

    public Completable delete(long id);

    public Completable deleteByEndpointId(long id);

    public Single<JsonObject> create(Information information);
}
