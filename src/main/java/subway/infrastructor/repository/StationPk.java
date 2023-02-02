package subway.infrastructor.repository;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
class StationPk implements Serializable {

    private Long id;

    public StationPk() {

    }

    public StationPk(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationPk stationPk = (StationPk) o;
        return Objects.equals(id, stationPk.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
