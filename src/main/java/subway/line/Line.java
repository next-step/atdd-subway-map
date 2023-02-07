package subway.line;

import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false, unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        Station firstStation = this.sections.get(0).getUpStation();
        stations.add(firstStation);
        for (Section section : this.sections) {
            stations.add(section.getDownStation());
        }
        return stations;
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addLineSection(Station upStation, Station downStation, int distance) {
        validStation(upStation, downStation);
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    private Station getFinalStation() {
        return this.sections.get(this.sections.size() - 1).getDownStation();
    }

    private void validStation(Station upStation, Station downStation) {
        validUpStation(upStation);
        validDownStation(downStation);
    }

    private void validUpStation(Station upStation) {
        if (!getFinalStation().equals(upStation)) {
            throw new IllegalArgumentException();
        }
    }

    private void validDownStation(Station downStation) {
        if (getStations().contains(downStation)) {
            throw new IllegalArgumentException();
        }
    }
}
