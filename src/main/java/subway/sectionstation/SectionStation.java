package subway.sectionstation;

import subway.section.Section;
import subway.station.Station;

import javax.persistence.*;

@Entity(name = "SECTION_STATION")
public class SectionStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    public SectionStation() {
    }

    public SectionStation(Section section, Station station) {
        this.station = station;
        this.section = section;
    }

    public Long getId() {
        return id;
    }

    public Station getStation() {
        return station;
    }

    public Section getSection() {
        return section;
    }
}
