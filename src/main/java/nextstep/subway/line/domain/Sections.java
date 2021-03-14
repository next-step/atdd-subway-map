package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        this.sections.add(section);
    }

    private boolean isSectionEmpty() {

        return this.sections.isEmpty();
    }

    public Station lastStationOfSections() {
        int sectionSize = this.sections.size();

        if (sectionSize <= 0) {
            return null;
        }

        return this.sections.get(sectionSize -1).getDownStation();
    }

    private boolean isLastStationOfLine(Station station) {
       return this.lastStationOfSections().getId().equals(station.getId());
    }

    public boolean isAddSectionAvailable(Section section) {

        if (isSectionEmpty()) {
            return true;
        }

        return isLastStationOfLine(section.getUpStation());
    }

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();

        if (!isSectionEmpty()) {
            stations.add(this.sections.get(0).getUpStation());

            this.sections.forEach(section -> {
                stations.add(section.getDownStation());
            });
        }

        return stations;
    }

    public void removeSectionByStationId(Long stationId) {
        for (int i=0 ; i < this.sections.size(); i++) {
            Section section = this.sections.get(i);
            if (section.getDownStation().getId().equals(stationId)) {
                this.sections.remove(i);
                break;
            }
        }
    }
}
