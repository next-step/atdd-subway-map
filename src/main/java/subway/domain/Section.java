package subway.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long distance;

    @OneToOne()
    private Station startStation;

    public Section() {}

    public Section(Station startStation, Long distance) {
        this.distance = distance;
        this.startStation = startStation;
    }

    public Long getId() {
        return id;
    }

    public Long getDistance() {
        return distance;
    }

    public Station getStartStation() {
        return startStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(id, section.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
