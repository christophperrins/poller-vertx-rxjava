package digital.vix.vertxdemo.service;

import digital.vix.vertxdemo.models.Information;
import digital.vix.vertxdemo.repository.EndpointRepository;
import digital.vix.vertxdemo.repository.InformationRepository;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.ResultSet;

public class InformationServiceImpl implements InformationService{

    private InformationRepository informationRepository;

    public InformationServiceImpl(InformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }
    public Flowable<JsonObject> all(){
        return informationRepository.all();
    };

    public Single<ResultSet> findById(long id){
        return informationRepository.findById(id);
    };

    public Single<ResultSet> findByEndPointId(long id){
        return informationRepository.findByEndPointId(id);
    }

    public Flowable<JsonObject> findByEndPointIds(long[] id){
        return informationRepository.findByEndPointIds(id);
    };

    public Completable update(Information information){
        return informationRepository.update(information);
    };

    public Completable delete(long id){
        return informationRepository.delete(id);
    };

    public Completable deleteByEndpointId(long endpointId){
        return informationRepository.deleteByEndpointId(endpointId);
    };

    public Single<Long> create(Information information) {
        return informationRepository.create(information);
    }
}
