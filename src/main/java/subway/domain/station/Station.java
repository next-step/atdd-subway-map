package subway.domain.station;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.sectionstation.SectionStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<SectionStation> sectionStations = new ArrayList<>();

    public Station(String name) {
        this.name = new Name(name);
    }


}
