package subway.section;

import subway.station.Station;

import java.util.List;
import java.util.stream.Collectors;

public class Sections {
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

    public boolean matchLastStation(Station station) {
        return getLastSection().getDownStation().equals(station);
    }

    public int getSize() { return sections.size(); }

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
}
