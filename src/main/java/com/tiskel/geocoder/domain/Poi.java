package com.tiskel.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Poi.
 */
@Entity
@Table(name = "poi")
public class Poi implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "street")
    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "post_code")
    private String postCode;

    @NotNull
    @Column(name = "lat", nullable = false)
    private Double lat;

    @NotNull
    @Column(name = "lng", nullable = false)
    private Double lng;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pois" }, allowSetters = true)
    private Source source;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "pois" }, allowSetters = true)
    private PoiGroup poiGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Poi id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Poi name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return this.street;
    }

    public Poi street(String street) {
        this.setStreet(street);
        return this;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuildingNumber() {
        return this.buildingNumber;
    }

    public Poi buildingNumber(String buildingNumber) {
        this.setBuildingNumber(buildingNumber);
        return this;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public Poi postCode(String postCode) {
        this.setPostCode(postCode);
        return this;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Double getLat() {
        return this.lat;
    }

    public Poi lat(Double lat) {
        this.setLat(lat);
        return this;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return this.lng;
    }

    public Poi lng(Double lng) {
        this.setLng(lng);
        return this;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Poi active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Source getSource() {
        return this.source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Poi source(Source source) {
        this.setSource(source);
        return this;
    }

    public PoiGroup getPoiGroup() {
        return this.poiGroup;
    }

    public void setPoiGroup(PoiGroup poiGroup) {
        this.poiGroup = poiGroup;
    }

    public Poi poiGroup(PoiGroup poiGroup) {
        this.setPoiGroup(poiGroup);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Poi)) {
            return false;
        }
        return id != null && id.equals(((Poi) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Poi{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", street='" + getStreet() + "'" +
            ", buildingNumber='" + getBuildingNumber() + "'" +
            ", postCode='" + getPostCode() + "'" +
            ", lat=" + getLat() +
            ", lng=" + getLng() +
            ", active='" + getActive() + "'" +
            "}";
    }
}
