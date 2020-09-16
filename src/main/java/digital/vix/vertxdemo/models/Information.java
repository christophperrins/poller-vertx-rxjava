package digital.vix.vertxdemo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Information {
    private Long id;
    @JsonProperty("endpoint_id")
    private Long endPointId;
    private String owner;
    private String street;
    private String postcode;
    private String city;

    public Information() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getEndPointId() {
        return endPointId;
    }

    public void setEndPointId(Long endPointId) {
        this.endPointId = endPointId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Information)) return false;
        Information that = (Information) o;
        return id.equals(that.id) &&
                owner.equals(that.owner) &&
                Objects.equals(street, that.street) &&
                Objects.equals(postcode, that.postcode) &&
                Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, street, postcode, city);
    }

    public boolean hasFieldsForUpdate() {
        if (id == null)
            return false;
        if (owner == null)
            return false;
        if (street == null)
            return false;
        if (city == null)
            return false;
        return true;
    }

	private Long endpointId;

	public Long getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(Long endpointId) {
		this.endpointId = endpointId;
	}
	
	
}
