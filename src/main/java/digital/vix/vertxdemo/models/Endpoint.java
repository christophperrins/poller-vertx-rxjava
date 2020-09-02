package digital.vix.vertxdemo.models;

public class Endpoint {

	private Long id;
	private String name;
	private String endpoint;
	private int frequency;

	public Endpoint() {
		super();
	}

	public Endpoint(Long id, String name, String endpoint, int frequency) {
		super();
		this.id = id;
		this.name = name;
		this.endpoint = endpoint;
		this.frequency = frequency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return "Endpoint [id=" + id + ", name=" + name + ", endpoint=" + endpoint + ", frequency=" + frequency + "]";
	}
	
	
}
