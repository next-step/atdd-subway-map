package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.exception.SectionNotMatchedException;

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
        sections.add(section);
    }

    private Section getDownEndStation() {
        int downEndStationIndex = getDownEndStationIndex();
        return sections.get(downEndStationIndex);
    }

    private int getDownEndStationIndex() {
        return sections.size() - 1;
    }
}
