package digital.vix.vertxdemo.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EndpointHistory {

	private Long id;
	@JsonProperty("endpoint_id")
	private Long endpointId;
	private String status;
	@JsonProperty("response_time")
	private Long responseTime;
	private Date timedate;

	public EndpointHistory() {
		super();
	}

	public EndpointHistory(Long id, Long endpointId, String status, Long responseTime, Date timedate) {
		super();
		this.id = id;
		this.endpointId = endpointId;
		this.status = status;
		this.responseTime = responseTime;
		this.timedate = timedate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}

	public Date getTimedate() {
		return timedate;
	}

	public void setTimedate(Date timedate) {
		this.timedate = timedate;
	}

	public String toString(String host) {
		return "Endpoint= " + host + ", status=" + status + ", responseTime=" + responseTime;
	}

}
