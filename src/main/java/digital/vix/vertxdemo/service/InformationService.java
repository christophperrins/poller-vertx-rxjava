package digital.vix.vertxdemo.service;

import digital.vix.vertxdemo.models.Information;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;

public interface InformationService {

    public Flowable<JsonObject> all();

    public Single<ResultSet> findById(long id);

    public Single<ResultSet> findByEndPointId(long id);

    public Flowable<JsonObject> findByEndPointIds(long[] id);

    public Completable update(Information information);

    public Completable delete(long id);

    public Completable deleteByEndpointId(long id);

    public Single<Long> create(Information information);

}
