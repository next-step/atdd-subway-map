package subway.station.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Station {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Builder
    private Station(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Station)) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(id, station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
