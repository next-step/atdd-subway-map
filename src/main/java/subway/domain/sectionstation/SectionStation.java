package subway.domain.sectionstation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.section.Section;
import subway.domain.station.Station;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class SectionStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sectionId", referencedColumnName = "id")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stationId", referencedColumnName = "id")
    private Station station;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Direction direction;

    public SectionStation(Section section, Station station, Direction direction) {
        this.section = section;
        this.station = station;
        this.direction = direction;
    }

}
