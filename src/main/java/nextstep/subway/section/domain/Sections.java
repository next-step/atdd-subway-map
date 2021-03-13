package nextstep.subway.section.domain;

import nextstep.subway.exception.CanNotMatchUpStationException;
import nextstep.subway.exception.CanNotRemoveSectionException;
import nextstep.subway.exception.ExistDownStationException;
import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public boolean isEmpty() {
        return sections.size() == 0;
    }

    public void add(Section section) {
        sections.add(section);
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public void removeSection() {
        checkRemoveSection();
        removeLastSection();
    }

    public void checkRemoveSection() {
        if(sections.size() == 1) {
            throw new CanNotRemoveSectionException();
        }
    }

    private Section removeLastSection() {
        return sections.remove(sections.size() - 1);
    }

    public List<Station> getStations() {
        return sections.stream()
                .sorted()
                .flatMap(Section::getStations)
                .distinct()
                .collect(Collectors.toList());
    }

    public void checkAddSection(Section section) {
        checkContain(section.getDownStation());
        checkValid(section.getUpStation());
    }

    public void checkContain(Station downStation) {
        if(getStations().contains(downStation)) {
            throw new ExistDownStationException();
        }
    }

    public void checkValid(Station upStation) {
        if(!getLastSection().isEqual(upStation)) {
            throw new CanNotMatchUpStationException();
        }
    }

    public List<Section> getSections() {
        return sections;
    }
}
