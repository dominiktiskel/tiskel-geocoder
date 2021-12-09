package com.tiskel.geocoder.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.tiskel.geocoder.domain.PoiGroup} entity.
 */
public class PoiGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String prefix;

    @NotNull
    private Boolean active;

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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PoiGroupDTO)) {
            return false;
        }

        PoiGroupDTO poiGroupDTO = (PoiGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, poiGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PoiGroupDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", prefix='" + getPrefix() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
