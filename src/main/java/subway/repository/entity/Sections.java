package subway.repository.entity;

import subway.exception.SubwayRuntimeException;
import subway.exception.message.SubwayErrorCode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public void addSection(Section section) {
        validate(section);
        sections.add(section);
    }

    private void validate(Section newSection) {
        if (sections.isEmpty()) {
            return;
        }

        if (newSection.addable(lastSection()) == false) {
            throw new SubwayRuntimeException(SubwayErrorCode.STATION_UPPER_SECTION);
        }

        if (newSection.addable(stations()) == false) {
            throw new SubwayRuntimeException(SubwayErrorCode.DOWN_STATION_HAS_BEEN_REGISTERED);
        }
    }

    public List<Station> getStations() {
        var stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        stations.add(getLastDownStation());

        return stations;
    }

    private Station getLastDownStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public List<Long> getStationIds() {
        return getStations()
                .stream()
                .map(Station::getId)
                .collect(Collectors.toList());
    }

    public List<Station> stations() {
        return sections.stream()
                .map(section -> List.of(section.getUpStation(), section.getDownStation()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toUnmodifiableList());
    }

    public void delete(long stationId) {
        if (sections.size() == 1) {
            throw new SubwayRuntimeException(SubwayErrorCode.SECTION_DELETE_ERROR);
        }

        Section lastSection = lastSection();
        if (lastSection.isDownStationId(stationId) == false) {
            throw new SubwayRuntimeException(SubwayErrorCode.ONLY_LAST_SEGMENT_CAN_BE_REMOVED);
        }

        sections.remove(lastSection);
    }

    private Section lastSection() {
        return sections.get(sections.size() - 1);
    }

    public int distance() {
        return sections.stream()
                .mapToInt(Section::getDistance)
                .sum();
    }
}
