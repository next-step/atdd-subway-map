package subway.repository.entity;

import subway.exception.SubwayRuntimeException;
import subway.exception.message.SubwayErrorCode;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    private static final int MIN_SECTION_SIZE = 1;

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

        if (!newSection.addable(lastSection())) {
            throw new SubwayRuntimeException(SubwayErrorCode.VALIDATE_SAME_UPPER_STATION);
        }

        if (!newSection.addable(getStations())) {
            throw new SubwayRuntimeException(SubwayErrorCode.ALREADY_REGISTERED_DOWN_STATION);
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

    public void delete(long stationId) {
        if (sections.size() == MIN_SECTION_SIZE) {
            throw new SubwayRuntimeException(SubwayErrorCode.CANNOT_DELETE_LAST_STATION);
        }

        Section lastSection = lastSection();
        if (!lastSection.isDownStationId(stationId)) {
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
