package digital.vix.vertxdemo.dto;

import digital.vix.vertxdemo.models.Endpoint;
import digital.vix.vertxdemo.models.Information;

public class EndpointMetaDataDto {

	private Endpoint endpoint;
	private Information metaData;

	public EndpointMetaDataDto(Endpoint endpoint, Information metaData) {
		super();
		this.endpoint = endpoint;
		this.metaData = metaData;
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public Information getMetaData() {
		return metaData;
	}

	public void setMetaData(Information metaData) {
		this.metaData = metaData;
	}

	
}
