package digital.vix.vertxdemo.service;

import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.models.EndpointHistory;
import digital.vix.vertxdemo.repository.EndpointHistoryRepository;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.vertx.core.json.JsonObject;

public class EndpointHistoryServiceImpl implements EndpointHistoryService {

	private EndpointHistoryRepository endpointHistoryRepository;

	private PollService pollService;

	public EndpointHistoryServiceImpl(EndpointHistoryRepository endpointHistoryRepository, PollService pollService) {
		super();
		this.endpointHistoryRepository = endpointHistoryRepository;
		this.pollService = pollService;
	}

	@Override
	public Flowable<JsonObject> readHistory(long id) {
		return endpointHistoryRepository.readHistory(id);
	}

	@Override
	public Completable addHistory(EndpointHistory endpointHistory) {
		return endpointHistoryRepository.addHistory(endpointHistory);
	}

	@Override
	public void pollEndpoint(Endpoint endpoint) {
		pollService.saveToCache(endpoint);
	}

	@Override
	public void stopPolling(long id) {
		pollService.removeFromCache(id);
	}

}
