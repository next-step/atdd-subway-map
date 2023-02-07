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
    private static final int MIN_SECTION_SIZE = 1;

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
        validAddStation(upStation, downStation);
        this.sections.add(new Section(this, upStation, downStation, distance));
    }

    private Station getFinalStation() {
        return getFinalSection().getDownStation();
    }

    private Section getFinalSection() {
        return this.sections.get(this.sections.size() - 1);
    }

    private void validAddStation(Station upStation, Station downStation) {
        validAddUpStation(upStation);
        validAddDownStation(downStation);
    }

    private void validAddUpStation(Station upStation) {
        if (!isFinalStation(upStation)) {
            throw new IllegalArgumentException();
        }
    }

    private void validAddDownStation(Station downStation) {
        if (getStations().contains(downStation)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isFinalStation(Station station) {
        return getFinalStation().equals(station);
    }

    private boolean isFinalStation(Long stationId) {
        return getFinalStation().getId().equals(stationId);
    }

    public void removeLineSection(Long stationId) {
        validSectionSize();
        validRemoveStation(stationId);

        this.sections.remove(getFinalSection());
    }

    private void validSectionSize() {
        if (this.sections.size() <= MIN_SECTION_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private void validRemoveStation(Long stationId) {
        if (!isFinalStation(stationId)) {
            throw new IllegalArgumentException();
        }
    }
}
