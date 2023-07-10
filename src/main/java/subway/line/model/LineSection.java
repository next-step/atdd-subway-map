package subway.line.model;

import lombok.NoArgsConstructor;
import subway.exception.SubwayBadRequestException;
import subway.line.constant.LineMessage;
import subway.station.model.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@NoArgsConstructor
public class LineSection {

    private static final long MINIMAL_SECTION_SIZE = 2L;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void add(Section section, Line line) {
        if (line.getLineSection().sections.size() > 1) {
            validUpStationInNewSectionIsDownStationInExistLine(section, line);
            validDownStationInNewSectionIsNotDuplicatedInExistLine(section, line);
        }
        section.setLine(line);
        this.sections.add(section);
    }

    public List<Station> getDownStations() {
        List<Station> stations = new ArrayList<>();
        for (Section section : this.sections) {
            stations.add(section.getDownStation());
        }
        return stations;
    }

    public int getStationsCount() {
        return this.sections.size();
    }

    public Section deleteSectionByStation(Station targetStation, Station downStation) {
        vaildStationsCountIsOverMinimalSectionSize();
        validRemoveStationIsDownStationInExistLine(targetStation, downStation);
        final int lastElementIndex = this.getStationsCount() - 1;
        Section lastSection = this.get(lastElementIndex);
        this.remove(lastSection);
        return lastSection;
    }

    public Section get(int index) {
        return sections.get(index);
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    private void validUpStationInNewSectionIsDownStationInExistLine(Section section, Line line) {
        if (!line.getDownStation().equals(section.getUpStation())) {
            throw new SubwayBadRequestException(LineMessage.DOWN_STATION_NOT_MATCH_WITH_UP_STATION.getCode(),
                    LineMessage.DOWN_STATION_NOT_MATCH_WITH_UP_STATION.getMessage());
        }
    }

    private void validDownStationInNewSectionIsNotDuplicatedInExistLine(Section section, Line line) {
        List<Station> stationsInLine = line.getStationsInSections();
        stationsInLine.stream()
                .filter(s -> s.equals(section.getDownStation()))
                .findAny()
                .ifPresent(e -> {
                    throw new SubwayBadRequestException(LineMessage.ADD_SECTION_STATION_DUPLICATION_VALID_MESSAGE.getCode(),
                            LineMessage.ADD_SECTION_STATION_DUPLICATION_VALID_MESSAGE.getMessage());
                });
    }

    private void vaildStationsCountIsOverMinimalSectionSize() {
        if (this.getStationsCount() < MINIMAL_SECTION_SIZE) {
            throw new SubwayBadRequestException(LineMessage.DOWN_STATION_MINIMAL_VALID_MESSAGE.getCode(),
                    LineMessage.DOWN_STATION_MINIMAL_VALID_MESSAGE.getFormatMessage(MINIMAL_SECTION_SIZE));
        }
    }

    private void validRemoveStationIsDownStationInExistLine(Station targetStation, Station downStation) {
        if (!downStation.equals(targetStation)) {
            throw new SubwayBadRequestException(LineMessage.SECTION_DELETE_LAST_STATION_VALID_MESSAGE.getCode(),
                    LineMessage.SECTION_DELETE_LAST_STATION_VALID_MESSAGE.getMessage());
        }
    }
}
