package nextstep.subway.domain.section;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import nextstep.subway.domain.exception.DomainException;
import nextstep.subway.domain.exception.DomainExceptionType;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Section> sectionList;

    public Sections() {
        this.sectionList = new ArrayList<>();
    }

    private Sections(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public static Sections of(Line line, Station upStation, Station downStation, Long distance) {
        var sectionList = new ArrayList<Section>();
        sectionList.add(new Section(line, upStation, downStation, distance));
        return new Sections(sectionList);
    }

    public void add(Line line, Station upStation, Station downStation, Long distance) {
        if (!getLast().getDownStation().getId().equals(upStation.getId())) {
            throw new DomainException(DomainExceptionType.DONT_MATCH_STATION_BETWEEN_SECTIONS);
        }
        this.sectionList.add(new Section(line, upStation, downStation, distance));
    }

    public void delete(Long stationId) {
        if (!getLast().getUpStation().getId().equals(stationId)) {
            throw new DomainException(DomainExceptionType.CAN_NOT_REMOVE_SECTION_IN_MIDDLE);
        }
    }

    public List<Station> getStations() {
        return this.sectionList.stream()
                .map(Section::getDownStation)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Section getLast() {
        return sectionList.get(sectionList.size() - 1);
    }

}
