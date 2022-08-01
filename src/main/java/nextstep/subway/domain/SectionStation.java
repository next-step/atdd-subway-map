package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class SectionStation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_station_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    public SectionStation() {
    }

    public SectionStation(Section section, Station station) {
        this.section = section;
        this.station = station;
        section.addSectionStation(this);
        station.addSectionStation(this);
    }

    public Section getSection() {
        return section;
    }

    public Station getStation() {
        return station;
    }
}
