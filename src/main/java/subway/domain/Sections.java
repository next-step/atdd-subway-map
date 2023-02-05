package subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final String CAN_DELETE_IF_MORE_THAN_ONE_SECTION_EXISTS = "구간이 1개인 경우 역을 삭제할 수 없습니다";
    private static final String CAN_DELETE_IF_LAST_STATION_MATCHES = "지하철 노선에 등록된 마지막 구간만 제거할 수 있습니다";
    private static final String CAN_ADD_IF_LAST_STATION_MATCHES = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다";

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void init(Section section) {
        this.sections.add(section);
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> section.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public void addSection(Section section) {
        matchingDownStationWithNewUpStation(section);
        sections.add(section);
    }

    private void matchingDownStationWithNewUpStation(Section newSection) {
        if (!getLastSection().getDownStation().equals(newSection.getUpStation())) {
            throw new IllegalArgumentException(CAN_ADD_IF_LAST_STATION_MATCHES);
        }
    }

    public void delete(Station station) {
        validationForDeleting(station);
        sections.remove(lastIndex());

    }

    private void validationForDeleting(Station station) {
        if (lessThenTwoSection()) {
            throw new IllegalArgumentException(CAN_DELETE_IF_MORE_THAN_ONE_SECTION_EXISTS);
        }
        if (!isDownStationEndWith(station)) {
            throw new IllegalArgumentException(CAN_DELETE_IF_LAST_STATION_MATCHES);
        }
    }

    private boolean isDownStationEndWith(Station station) {
        return getLastSection().isDownStationEndWith(station);
    }

    private Section getLastSection() {
        return sections.get(lastIndex());
    }

    private boolean lessThenTwoSection() {
        return this.sections.size() < 2;
    }

    private int lastIndex() {
        return sections.size() - 1;
    }
}
