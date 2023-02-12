package subway.station.domain.line;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.section.Section;
import subway.station.domain.station.Station;
import subway.station.global.error.exception.ErrorCode;
import subway.station.global.error.exception.InvalidValueException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    private String name;
    private String color;

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    @Builder
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Station upStation, Station downStation, Long distance) {
        addValidationCheck(upStation, downStation);
        this.getSections().add(
                Section.builder()
                        .upStation(upStation)
                        .downStation(downStation)
                        .distance(distance)
                        .line(this)
                        .build());
    }

    public void deleteSection(Station station) {
        Section section = findSection(station);
        deleteValidationCheck(section);
        this.getSections().remove(section);
    }

    private void deleteValidationCheck(Section section) {
        if(this.getSections().size() == 1) {
            throw new InvalidValueException(ErrorCode.LINE_HAS_ONLY_ONE_SECTION);
        }
        if(!sections.get(sections.size()-1).equals(section)) {
            throw new InvalidValueException(ErrorCode.SECTION_IS_NOT_END_OF_LINE);
        }
    }

    private void addValidationCheck(Station upStation, Station downStation) {
        if (this.getSections().size() == 0) {
            return;
        }
        if (!sections.get(sections.size() - 1).getDownStation().equals(upStation)) {
            throw new InvalidValueException(ErrorCode.MISMATCHED_BETWEEN_UP_STATION_OF_NEW_SECTION_AND_DOWN_STATION_OF_LINE);
        }
        for (Section section : sections) {
            if (section.getUpStation().equals(downStation) || section.getDownStation().equals(downStation)) {
                throw new InvalidValueException(ErrorCode.ALREADY_REGISTERED_IN_LINE);
            }
        }
    }

    private Section findSection(Station station) {
        return this.getSections().stream()
                .filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
                .findFirst()
                .orElseThrow(() -> new InvalidValueException(ErrorCode.STATION_NOT_EXISTS_SECTION));
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return Collections.emptyList();
        }
        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        sections
                .forEach(section -> stations.add(section.getDownStation()));
        return stations;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
