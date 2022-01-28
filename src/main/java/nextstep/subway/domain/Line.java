package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.exception.DeleteSectionException;
import nextstep.subway.exception.SectionNotValidException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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
        return sections.getAllSections();
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        if (!validateUpStation(section.getUpStation())) {
            throw new SectionNotValidException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
        }

        if (!validateDownStation(section.getDownStation())) {
            throw new SectionNotValidException("하행역은 현재 등록되어있는 역일 수 없습니다.");
        }

        sections.add(section);

        if (section.getLine() != this) {
            section.changeLine(this);
        }
    }

    public boolean validateUpStation(Station upStation) {
        if (sections.isEmpty()) {
            return true;
        }

        return sections.getLastDownStation() == upStation;
    }

    public boolean validateDownStation(Station downStation) {
        List<Station> upStations = sections.getSectionStations(Section::getUpStation);
        List<Station> downStations = sections.getSectionStations(Section::getDownStation);

        if (upStations.contains(downStation)) {
            return false;
        }

        return !downStations.contains(downStation);
    }


    public void deleteSection(long lastDownStationId) {
        Station lastDownStation = sections.getLastDownStation();

        if (!sections.isAvailableDelete()) {
            throw new DeleteSectionException("구간이 1개 이하인 경우 역을 삭제할 수 없습니다.");
        }

        if (!lastDownStation.isSameStation(lastDownStationId)) {
            throw new DeleteSectionException("구간에 일치하는 하행 종점역이 없습니다.");
        }

        Section delete = sections.getByDownStation(lastDownStation);

        sections.remove(delete);
    }

    public List<StationResponse> getAllStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }

        return sections.getAllStations()
            .stream()
            .map(StationResponse::ofStation)
            .collect(Collectors.toList());
    }
}
