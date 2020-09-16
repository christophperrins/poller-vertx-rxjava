package digital.vix.vertxdemo.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.models.EndpointHistory;
import digital.vix.vertxdemo.repository.EndpointHistoryRepository;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

public class PollServiceImpl implements PollService {

	private Vertx vertx;
	private EndpointHistoryRepository endpointHistoryRepository;
	private Map<Long, Endpoint> endpoints = new HashMap<Long, Endpoint>();
	
	public PollServiceImpl(Vertx vertx, EndpointHistoryRepository endpointHistoryRepository) {
		this.vertx = vertx;
		this.endpointHistoryRepository = endpointHistoryRepository;
	}

	@Override
	public void saveToCache(Endpoint endpoint) {
		if (endpoint.equals(endpoints.get(endpoint.getId()))) {
			return;
		}
		endpoints.put(endpoint.getId(), endpoint);
		startPolling(endpoint);
	}

	public void startPolling(Endpoint endpoint) {
		System.out.println("POLLING: " + endpoint.getName() + " every " + endpoint.getFrequency() + "ms");
		WebClientOptions webClientOptions = new WebClientOptions();
		webClientOptions.setTrustAll(true).setDefaultPort(80);
		WebClient webClient = WebClient.create(vertx, webClientOptions);

		vertx.setPeriodic(endpoint.getFrequency(), timerId -> {
			// stop polling if deleted or changed
			if (endpoints.get(endpoint.getId()) == null || !endpoint.equals(endpoints.get(endpoint.getId()))) {
				System.out.println("STOP POLLING: " + endpoint.getName());
				webClient.close();
				vertx.cancelTimer(timerId);
			} else {
				EndpointHistory endpointHistory = new EndpointHistory();
				endpointHistory.setEndpointId(endpoint.getId());
				final long start = System.currentTimeMillis();
				webClient.get(80, endpoint.getEndpoint(), "/").send(ar -> {
					endpointHistory.setStatus(String.valueOf(ar.result().statusCode()));
					long end = System.currentTimeMillis();
					endpointHistory.setResponseTime(end - start);
					endpointHistory.setTimedate(new Date());
					System.out.println(endpointHistory.toString(endpoint.getEndpoint()) + " @frequency=" + endpoint.getFrequency()+"ms");
					endpointHistoryRepository.addHistory(endpointHistory).subscribe();
				});
			}
		});
	}

	@Override
	public void removeFromCache(long id) {
		endpoints.remove(id);
	}

}
