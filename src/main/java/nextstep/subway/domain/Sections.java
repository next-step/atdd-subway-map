package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.InvalidRequestException;

@Embeddable
public class Sections {
    private static final int SECTION_MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void init(Section firstSection) {
        sections.add(firstSection);
    }

    public void add(Section section) {
        validateSection(section);
        sections.add(section);
    }

    private void validateSection(Section section) {
        Station newUpStation = section.getUpStation();
        if (sections.size() > 0 && !getLastSection().isDownStation(newUpStation)) {
            throw new InvalidRequestException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }

        getStations().stream()
            .filter(section::isDownStation)
            .findAny().ifPresent(s -> {throw new InvalidRequestException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");});
    }

    public void remove(Station station) {
        if (sections.size() <= SECTION_MIN_SIZE) {
            throw new InvalidRequestException("상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없습니다.");
        }

        if (!getLastSection().isDownStation(station)) {
            throw new InvalidRequestException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있습니다.");
        }

        sections.remove(getLastSection());
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(getFirstSection().getUpStation());
        stations.addAll(
            sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList())
        );
        return stations;
    }

    private Section getFirstSection() {
        return sections.get(0);
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }
}
