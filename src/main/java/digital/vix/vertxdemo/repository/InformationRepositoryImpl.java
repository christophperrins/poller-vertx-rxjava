package digital.vix.vertxdemo.repository;

import digital.vix.vertxdemo.models.Information;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;
import io.vertx.reactivex.ext.sql.SQLClient;

public class InformationRepositoryImpl implements InformationRepository {

    private SQLClient sqlClient;

    public InformationRepositoryImpl(SQLClient sqlClient) {
        this.sqlClient = sqlClient;
    }

    @Override
    public Flowable<JsonObject> all() {
        return sqlClient.rxQuery("SELECT * FROM information")
                .flatMapPublisher(resultSet -> Flowable.fromIterable(resultSet.getRows()));
    }

    @Override
    public Single<ResultSet> findByEndPointId(long id) {
        return sqlClient
              .rxQueryWithParams("SELECT * FROM information where endpoint_id = ?", new JsonArray().add(id));
    };

    @Override
    public Single<ResultSet> findById(long id) {
        return sqlClient.rxQueryWithParams("SELECT * FROM information where id = ?", new JsonArray().add(id));
    };


    @Override
    public Flowable<JsonObject> findByEndPointIds(long[] ids) {
        JsonArray idsPara = new JsonArray();
        StringBuilder queryExtension = new StringBuilder("");
        for (long id:  ids){
            idsPara.add(id);
            queryExtension.append("?, ");
        }
        queryExtension = (queryExtension.length() > 0) ? queryExtension.delete(queryExtension.length() - 2, queryExtension.length()) : queryExtension;

        return sqlClient
                .rxQueryWithParams("SELECT * FROM information where endpoint_id IN (" + queryExtension + ")", idsPara)
                .flatMapPublisher(resultSet -> Flowable.fromIterable(resultSet.getRows()));
    };

    public Completable update(Information information){
        return sqlClient.rxUpdateWithParams("UPDATE endpoints set owner = ?, street = ?, postcode = ?, city = ? where id = ?",
                new JsonArray().add(information.getOwner()).add(information.getStreet()).add(information.getPostcode()).add(information.getCity()))
                .ignoreElement();
    };

    public Completable delete(long id){
        return sqlClient.rxUpdateWithParams("DELETE FROM information where id = ?", new JsonArray().add(id))
                .ignoreElement();
    };

    public Completable deleteByEndpointId(long id){
        return sqlClient.rxUpdateWithParams("DELETE FROM information where endpoint_id = ?", new JsonArray().add(id))
                .ignoreElement();
    };

    public Single<Long> create(Information information) {
        return sqlClient
                .rxUpdateWithParams(
                        "INSERT INTO information (owner, street, postcode, city, endpoint_id) values (?, ?, ?, ?, ?)",
                        new JsonArray().add(information.getOwner()).add(information.getStreet())
                                .add(information.getPostcode()).add(information.getCity()).add(information.getEndPointId()))
                .map(updateResult -> updateResult.getKeys().getLong(0));
    }

}
