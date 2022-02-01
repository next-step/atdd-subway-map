package nextstep.subway.domain.section;

import nextstep.subway.domain.station.Station;
import nextstep.subway.handler.error.custom.BusinessException;
import nextstep.subway.handler.validator.SectionValidator;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList;

    public Sections() {
        this.sectionList = new ArrayList<>();
    }

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        sectionList.forEach(section -> section.push(stations));

        return stations;
    }

    public boolean hasAnyMatchedDownStation(Station upStation) {
        if(sectionList.isEmpty()) {
            return true;
        }
        return sectionList.get(sectionList.size() - 1).hasDownStation(upStation);
    }

    public boolean hasStation(Station downStation) {
        return sectionList.stream().anyMatch(section -> section.hasStation(downStation));
    }

    public List<Section> getSectionList() {
        return new ArrayList<>(sectionList);
    }

    public Section delete(Station station) {
        Section section = sectionList.get(sectionList.size() - 1);
        SectionValidator.properDelete(section, station);
        sectionList.remove(section);

        return section;
    }

    public void add(Section section) {
        this.sectionList.add(section);
    }

    public boolean hasOneSection() {
        return sectionList.size() == 1;
    }
}
