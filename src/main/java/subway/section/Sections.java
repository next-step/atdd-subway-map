package subway.section;

import subway.exception.SubwayBadRequestException;
import subway.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {

    public static final int MIN_LINE_SECTION_SIZE = 1;

    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Section> getSections() {
        return sections;
    }

    public boolean contains(Station downStation) {
        return sections.stream()
                .anyMatch(section-> section.hasStation(downStation));
    }

    public boolean isLastStation(Station station) {
        return getLastSection().getDownStation().equals(station);
    }

    public List<Station> getAllStation() {
        var stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        if (!sections.isEmpty()) {
            stations.add(getLastSection().getDownStation());
        }

        return stations;
    }

    public Section getLastSection() {
        int size = sections.size();
        if (size == 0) {
            throw new IllegalStateException("section size is zero");
        }
        return sections.get(size - 1);
    }

    public void checkLastStationEquals(Station station) {
        if (!isLastStation(station)) {
            throw new SubwayBadRequestException("station(" + station + ") is not downStation of the line");
        }
    }

    public void checkSizeBeforeDelete() {
        if (sections.size() <= MIN_LINE_SECTION_SIZE) {
            throw new SubwayBadRequestException("cannot delete when line has less than 1 section, current section size: " + sections.size());
        }
    }

    public void checkLineAlreadyContains(Station station) {
        if (getAllStation().contains(station)) {
            throw new SubwayBadRequestException("line already contains the station, " + station);
        }
    }
}
