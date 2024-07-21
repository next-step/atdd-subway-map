package subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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

    public void addFirstSection(Section section) {
        this.distance = section.getDistance();
        this.upStation = section.getUpStation();
        this.downStation = section.getDownStation();
        this.sections.add(section);
        section.assignSubwayLine(this);
    }

    public void updateBasicInfo(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getNonEndStations() {
        return getStations()
                .stream()
                .filter(station -> !station.equals(upStation) && !station.equals(downStation))
                .collect(Collectors.toList());
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
