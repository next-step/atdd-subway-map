package nextstep.subway.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Section {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Station> stations = new ArrayList<>();

    private Long distance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "line_id")
    private Line line;

    public Section() {
    }

    public Section(Station upStation, Station downStation, Long distance) {
        this.stations.add(upStation);
        this.stations.add(downStation);
        this.distance = distance;
        upStation.setSection(this);
        downStation.setSection(this);
    }
    public void update(Station upStation, Station downStation, Long distance) {
        this.getUpStation().update(upStation);
        this.getDownStation().update(downStation);
        this.distance = distance != null ? distance : this.distance;
    }

    public Station getUpStation() {
        return this.stations.get(0);
    }

    public Station getDownStation() {
        return this.stations.get(1);
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

    public void setLine(Line line) {
        this.line = line;
    }
}
