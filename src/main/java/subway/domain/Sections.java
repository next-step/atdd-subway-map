package subway.domain;

import subway.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;

public class Sections {

    private static final long NON_SECTION_DISTANCE = 0;

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        validateSize(sections);
        this.sections = new ArrayList<>(sections);
    }

    private void validateSize(List<Section> sections) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("구간이 존재하지 않습니다.");
        }
    }

    public void validateRegisterStationBy(Station upStation, Station downStation) {
        if (sections.stream().anyMatch(section -> section.isUpStation(upStation))) {
            throw new ApplicationException("새로운 구간의 상행역은 노선의 하행 종점역에만 등록할 수 있습니다.");
        }

        if (sections.stream().anyMatch(section -> section.isDownStation(downStation))) {
            throw new ApplicationException("새로운 구간의 하행역은 노선의 역에 등록할 수 없습니다.");
        }
    }

    public void validateLineDistance(long distance) {
        long sectionDistance = calculateSectionDistance(distance);
        if (sectionDistance <= NON_SECTION_DISTANCE) {
            throw new ApplicationException("새로운 구간의 길이는 노선의 길이 보다 작거나 같을 수 없습니다.");
        }
    }

    private long totalLineDistance() {
        return sections.stream()
                .mapToLong(Section::distance)
                .sum();
    }

    public long calculateSectionDistance(long distance) {
        return distance - totalLineDistance();
    }

}
