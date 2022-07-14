package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.SectionNotDownEndStationException;
import nextstep.subway.exception.SectionNotMatchedException;
import nextstep.subway.exception.StationAlreadyExistInSectionException;

@Embeddable
public class Sections {

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
        if (!downEndStation.isMatchedStationId(stationId)) {
            throw new SectionNotDownEndStationException();
        }
        removeDownEndSection();
    }

    private void removeDownEndSection() {
        sections.remove(getDownEndStationIndex());
    }
}
