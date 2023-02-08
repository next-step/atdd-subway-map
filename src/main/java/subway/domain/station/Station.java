package subway.domain.station;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.sectionstation.SectionStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @OneToMany(mappedBy = "station")
    private final List<SectionStation> sectionStations = new ArrayList<>();

    public Station(String name) {
        this.name = new Name(name);
    }

    public String getNameValue() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
