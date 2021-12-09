package com.tiskel.geocoder.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.geocoder.domain.Poi} entity.
 */
public class PoiDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String street;

    private String buildingNumber;

    private String postCode;

    @NotNull
    private Double lat;

    @NotNull
    private Double lng;

    @NotNull
    private Boolean active;

    private SourceDTO source;

    private PoiGroupDTO poiGroup;

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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public SourceDTO getSource() {
        return source;
    }

    public void setSource(SourceDTO source) {
        this.source = source;
    }

    public PoiGroupDTO getPoiGroup() {
        return poiGroup;
    }

    public void setPoiGroup(PoiGroupDTO poiGroup) {
        this.poiGroup = poiGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoiDTO)) {
            return false;
        }

        PoiDTO poiDTO = (PoiDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, poiDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoiDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", street='" + getStreet() + "'" +
            ", buildingNumber='" + getBuildingNumber() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", lat=" + getLat() +
            ", lng=" + getLng() +
            ", active='" + getActive() + "'" +
            ", source=" + getSource() +
            ", poiGroup=" + getPoiGroup() +
            "}";
    }
}
