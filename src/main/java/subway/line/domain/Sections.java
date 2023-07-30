package subway.line.domain;

import subway.common.error.InvalidSectionRequestException;
import subway.section.domain.Section;
import subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    Long getOriginSectionId() {
        return getFirst().getUpStationId();
    }

    Long getTerminalSectionId() {
        return getLast().getDownStationId();
    }

    public Section getFirst() {
        return sections.get(0);
    }

    public Section getLast() {
        return sections.get(sections.size() - 1);
    }

    public void add(Section section) {
        if (!sections.isEmpty()) {
            validateUpStationId(section);
            validateDownStationId(section);
        }

        sections.add(section);
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .toList();
    }

    public Integer getTotalDistance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }

    public boolean hasLessThanTwoSections() {
        return sections.size() < 2;
    }

    private void validateUpStationId(Section section) {
        Line line = section.getLine();
        Long upStationId = section.getUpStationId();

        if (!Objects.equals(upStationId, line.getTerminalStationId())) {
            throw new InvalidSectionRequestException("해당 노선의 하행종점역이 아닌 역이 상행역으로 설정되었습니다.",
                    Map.of(
                            "lineId", line.getId().toString(),
                            "upStationId", upStationId.toString(),
                            "downStationId", section.getDownStationId().toString()
                    ));
        }
    }

    private void validateDownStationId(Section section) {
        Line line = section.getLine();
        Station downStation = section.getDownStation();
        if (line.getStations().contains(downStation)) {
            throw new InvalidSectionRequestException("이미 노선에 등록된 역을 새로운 구간의 하행역으로 등록하였습니다.",
                    Map.of(
                            "lineId", line.getId().toString(),
                            "upStationId", section.getUpStationId().toString(),
                            "downStationId", section.getDownStationId().toString()
                    ));
        }
    }
}
