package nextstep.subway.domain;

import nextstep.subway.exception.ErrorCode;
import nextstep.subway.exception.unchecked.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        if (isInvalidSize(sections)) {
            throw new SectionException(ErrorCode.INVALID_SIZE_SECTIONS);
        }
        this.sections = sections;
    }

    private boolean isInvalidSize(List<Section> sections) {
        return sections.size() < 1;
    }

    public void add(Section section) {
        this.sections.add(section);
    }

    public void changeUpStation(Station upStation) {
        Section firstSection = sections.get(0);
        firstSection.changeUpStation(upStation);
    }

    public void changeDownStation(Station downStation) {
        Section lastSection = sections.get(sections.size() - 1);
        lastSection.changeDownStation(downStation);
    }

    public List<Long> getAllStationId() {
        Set<Long> stationIds = new HashSet<>();
        sections.forEach(section -> {
                    stationIds.add(section.getUpStationId());
                    stationIds.add(section.getDownStationId());
                }
        );
        return new ArrayList<>(stationIds);
    }

    public List<Station> getStations() {
        final List<Station> stations = new ArrayList<>();
        stations.add(sections.get(0).getUpStation());
        for (Section section : sections) {
            stations.add(section.getDownStation());
        }

        return stations;
    }
}
