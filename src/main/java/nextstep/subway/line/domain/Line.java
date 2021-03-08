package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.exceptions.AlreadyExistsEntityException;
import nextstep.subway.exceptions.NotEqualsStationException;
import nextstep.subway.exceptions.OnlyOneSectionException;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {
    public static final String ONLY_DOWN_STATION_CAN_DELETED = "하행 종점역만 삭제가 가능합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.addSection(section);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        boolean isPresent = sections.stream()
                .anyMatch(section::equals);

        if (!isPresent) {
            sections.add(section);
            section.updateLine(this);
        }
    }

    public List<StationResponse> getStationsAll() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        List<StationResponse> responses = new ArrayList<>();
        responses.add(StationResponse.of(sections.get(0).getUpStation()));

        sections.forEach(section -> responses.add(StationResponse.of(section.getDownStation())));

        return responses;
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public void isValidSection(Section newSection) {
        Station downStation = getDownStation();
        if (!newSection.getUpStation().equals(downStation)) {
            throw new NotEqualsStationException();
        }

        if (isExistsDownStation(newSection.getDownStation())) {
            throw new AlreadyExistsEntityException();
        }

    }

    private Station getDownStation() {
        return getSections().get(getSections().size() - 1).getDownStation();
    }

    private boolean isExistsDownStation(Station station) {
        return sections.stream()
                .anyMatch(section -> isEqualDownAndUpStation(station, section));
    }

    private boolean isEqualDownAndUpStation(Station station, Section section) {
        return Objects.equals(section.getUpStation(), station) || Objects.equals(section.getDownStation(), station);
    }

    public void deleteLastSection(Long stationId) {
        isValidDeleteSection(stationId);

        sections.remove(getLastSection());
    }

    private void isValidDeleteSection(Long stationId) {
        Section lastSection = getLastSection();
        boolean isEqualDownStationAndTarget = lastSection.getDownStation()
                .getId()
                .equals(stationId);

        if (!isEqualDownStationAndTarget) {
            throw new IllegalArgumentException(ONLY_DOWN_STATION_CAN_DELETED);
        }

        if (sections.size() == 1) {
            throw new OnlyOneSectionException();
        }
    }
}
