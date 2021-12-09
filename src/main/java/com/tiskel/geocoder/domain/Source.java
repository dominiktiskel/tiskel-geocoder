package com.tiskel.geocoder.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Source.
 */
@Entity
@Table(name = "source")
public class Source implements Serializable {

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
    @Column(name = "active", nullable = false)
    private Boolean active;

    @OneToMany(mappedBy = "source")
    @JsonIgnoreProperties(value = { "source", "poiGroup" }, allowSetters = true)
    private Set<Poi> pois = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Source id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Source name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Source active(Boolean active) {
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
            this.pois.forEach(i -> i.setSource(null));
        }
        if (pois != null) {
            pois.forEach(i -> i.setSource(this));
        }
        this.pois = pois;
    }

    public Source pois(Set<Poi> pois) {
        this.setPois(pois);
        return this;
    }

    public Source addPoi(Poi poi) {
        this.pois.add(poi);
        poi.setSource(this);
        return this;
    }

    public Source removePoi(Poi poi) {
        this.pois.remove(poi);
        poi.setSource(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Source)) {
            return false;
        }
        return id != null && id.equals(((Source) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Source{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
