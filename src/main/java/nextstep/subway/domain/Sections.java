package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.OneSectionDeleteException;
import nextstep.subway.exception.SectionNotDownEndStationException;
import nextstep.subway.exception.SectionNotMatchedException;
import nextstep.subway.exception.StationAlreadyExistInSectionException;

@Embeddable
public class Sections {
    private static final int ONE_SECTION = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {

    }

    public Sections(Section section) {
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    public void addSection(Section section) {
        Section downEndStation = getDownEndStation();
        if (!downEndStation.isMatched(section)) {
            throw new SectionNotMatchedException();
        }

        if(stationsContain(section.getDownStation())){
            throw new StationAlreadyExistInSectionException();
        }
        sections.add(section);
    }

    private boolean stationsContain(Station station) {
        return sections.stream()
                .anyMatch(section -> section.contains(station));
    }

    private Section getDownEndStation() {
        int downEndStationIndex = getDownEndStationIndex();
        return sections.get(downEndStationIndex);
    }

    private int getDownEndStationIndex() {
        return sections.size() - 1;
    }

    public void deleteSection(Long stationId) {
        Section downEndStation = getDownEndStation();
        if(sections.size() <= ONE_SECTION) {
            throw new OneSectionDeleteException();
        }

        if (!downEndStation.isMatchedStationId(stationId)) {
            throw new SectionNotDownEndStationException();
        }
        removeDownEndSection();
    }

    private void removeDownEndSection() {
        sections.remove(getDownEndStationIndex());
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());

        for (Section section : sections) {
            stations.add(section.getDownStation());
        }

        return stations;
    }
}
