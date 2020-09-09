package digital.vix.vertxdemo.service;

import digital.vix.vertxdemo.models.Endpoint;

public interface PollService {

	public void saveToCache(Endpoint endpoint);
	
	public void removeFromCache(long id);
}
