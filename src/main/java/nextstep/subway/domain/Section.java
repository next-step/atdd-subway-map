package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Section {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    private Long distance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "line_id")
    private Line line;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<SectionStation> sectionStations = new ArrayList<>();

    public Section() {
    }

    public Section(Long distance) {
        this.distance = distance;
    }

    public void update(Station upStation, Station downStation, Long distance) {
        this.getUpStation().update(upStation);
        this.getDownStation().update(downStation);
        this.distance = distance != null ? distance : this.distance;
    }

    public void addSectionStation(SectionStation sectionStation) {
        this.sectionStations.add(sectionStation);
    }

    public Station getUpStation() {
        return this.sectionStations.get(0).getStation();
    }

    public Station getDownStation() {
        return this.sectionStations.get(1).getStation();
    }

    public Long getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return this.getUpStation().getId();
    }

    public Long getDownStationId() {
        return this.getDownStation().getId();
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        for (SectionStation sectionStation : this.sectionStations) {
            stations.add(sectionStation.getStation());
        }

        return stations;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
