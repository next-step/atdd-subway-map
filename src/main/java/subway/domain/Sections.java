package subway.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import subway.exception.SectionCannotAddException;
import subway.exception.SectionCannotRemoveException;

@Embeddable
public class Sections {
    private static final String NOT_LAST_SECTION_DOWN_STATION_EXCEPTION_MESSAGE = "새로운 구간의 상행역은 기존 노선에 등록되어 있는 하행 종점역이여야합니다.";
    private static final String ALREADY_EXIST_SECTION_IN_LINE_EXCEPTION_MESSAGE = "새로운 구간의 하행역은 기존 노선에 등록된 역이지 않아야 합니다.";
    private static final String ONLY_ONE_SECTION_EXIST_EXCEPTION_MESSAGE = "구간이 1개인 경우 제거할 수 없습니다.";
    private static final String NOT_REGISTERED_DOWN_STATION_EXCEPTION_MESSAGE = "노선에 등록된 하행 종점역만 제거할 수 있습니다.";
    
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void initSection(Section section) {
        this.sections.add(section);
    }

    public void addSection(Section section) {
        validateAddSection(section);
        this.sections.add(section);
    }

    public void removeSection(Long stationId) {
        validateRemoveSection(stationId);
        var lastSection = sections.get(sections.size() - 1);
        sections.remove(lastSection);
    }

    public Long getDistance() {
        return sections.stream()
            .map(Section::getDistance)
            .reduce(0L, Long::sum);
    }

    public List<Section> getValue() {
        return sections;
    }

    private void validateAddSection(Section section) {
        Section lastSection = getLastSection();
        if (!lastSection.canAddSection(section)) {
            throw new SectionCannotAddException(NOT_LAST_SECTION_DOWN_STATION_EXCEPTION_MESSAGE);
        }

        Optional<Station> matchedStation = sections.stream()
            .map(s -> List.of(s.getUpStation(), s.getDownStation()))
            .flatMap(Collection::stream)
            .filter(station -> station.isSameStation(section.getDownStation()))
            .findAny();

        if (matchedStation.isPresent()) {
            throw new SectionCannotAddException(ALREADY_EXIST_SECTION_IN_LINE_EXCEPTION_MESSAGE);
        }
    }

    private void validateRemoveSection(Long stationId) {
        if (sections.size() <= 1) {

            throw new SectionCannotRemoveException(ONLY_ONE_SECTION_EXIST_EXCEPTION_MESSAGE);
        }

        Section lastSection = getLastSection();
        if (!lastSection.canRemoveSection(stationId)) {
            throw new SectionCannotRemoveException(NOT_REGISTERED_DOWN_STATION_EXCEPTION_MESSAGE);
        }
    }

    private Section getLastSection() {
        return sections.get(getLastSectionIndex());
    }

    private int getLastSectionIndex() {
        return sections.size() - 1;
    }
}
