package subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubwayLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    private Long distance = 0L;

    @ManyToOne
    @JoinColumn(name = "up_station_id", nullable = false)
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id", nullable = false)
    private Station downStation;

    @OneToMany(mappedBy = "subwayLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    private SubwayLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static SubwayLine of(String name, String color, Section section) {
        var subwayLine = new SubwayLine(name, color);
        subwayLine.addFirstSection(section);
        return subwayLine;
    }

    private void addFirstSection(Section section) {
        this.distance = section.getDistance();
        this.upStation = section.getUpStation();
        this.downStation = section.getDownStation();
        this.sections.add(section);
        section.assignSubwayLine(this);
    }

    public void addSection(Section section) {
        if (!canAddSection(section)) {
            throw new UnsupportedOperationException();
        }
        this.distance = this.distance + section.getDistance();
        this.downStation = section.getDownStation();
        this.sections.add(section);
        section.assignOrder(this.sections.size());
        section.assignSubwayLine(this);
    }


    private boolean canAddSection(Section section) {
        var sectionUpStationId = section.getUpStation().getId();
        if (!Objects.equals(sectionUpStationId, this.downStation.getId())) {
            return false;
        }
        var sectionDownStationId = section.getDownStation().getId();
        return !this.hasStation(sectionDownStationId);
    }


    public void updateBasicInfo(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void removeStation(Long stationId) {
        if (!canRemove(stationId)) throw new UnsupportedOperationException();
        this.sections.sort(Comparator.comparingInt(Section::getOrder));
        var removedSection = this.sections.remove(this.sections.size() - 1);
        this.downStation = this.sections.get(this.sections.size() - 1).getDownStation();
        distance -= removedSection.getDistance();
    }

    private boolean canRemove(Long stationId) {
        if (this.sections.size() < 2) return false;
        return Objects.equals(stationId, this.downStation.getId());
    }

    private boolean hasStation(Long stationId) {
        return this.getStations()
                .stream()
                .anyMatch(s -> Objects.equals(stationId, s.getId()));
    }

    public List<Station> getStations() {
        return Stream.concat(
                        Stream.of(upStation, downStation),
                        sections.stream()
                                .flatMap(section -> section.getStations().stream())
                )
                .distinct()
                .collect(Collectors.toList());
    }
}
