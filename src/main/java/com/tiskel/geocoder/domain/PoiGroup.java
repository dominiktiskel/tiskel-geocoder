package com.tiskel.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PoiGroup.
 */
@Entity
@Table(name = "poi_group")
public class PoiGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "prefix", nullable = false)
    private String prefix;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "poiGroup")
    @JsonIgnoreProperties(value = { "source", "poiGroup" }, allowSetters = true)
    private Set<Poi> pois = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PoiGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PoiGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public PoiGroup prefix(String prefix) {
        this.setPrefix(prefix);
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getActive() {
        return this.active;
    }

    public PoiGroup active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Poi> getPois() {
        return this.pois;
    }

    public void setPois(Set<Poi> pois) {
        if (this.pois != null) {
            this.pois.forEach(i -> i.setPoiGroup(null));
        }
        if (pois != null) {
            pois.forEach(i -> i.setPoiGroup(this));
        }
        this.pois = pois;
    }

    public PoiGroup pois(Set<Poi> pois) {
        this.setPois(pois);
        return this;
    }

    public PoiGroup addPoi(Poi poi) {
        this.pois.add(poi);
        poi.setPoiGroup(this);
        return this;
    }

    public PoiGroup removePoi(Poi poi) {
        this.pois.remove(poi);
        poi.setPoiGroup(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoiGroup)) {
            return false;
        }
        return id != null && id.equals(((PoiGroup) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoiGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", prefix='" + getPrefix() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
