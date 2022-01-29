package nextstep.subway.domain;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected Station() {
    }

    public Station(String name) {
        validateBlankName(name);
        this.name = name;
    }

    private void validateBlankName(final String name) {
        if (Strings.isBlank(name)) {
            throw new IllegalArgumentException("blank station name occurred");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Station station = (Station) o;
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
