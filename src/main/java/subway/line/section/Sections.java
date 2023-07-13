package subway.line.section;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import subway.common.exception.BusinessException;
import subway.line.Line;
import subway.station.Station;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public Sections(Line line, Station upStationId, Station downStationId) {
        this(List.of(new Section(line, upStationId, downStationId, 1)));
    }

    public void add(Line line, Station upStation, Station downStation) {
        int sequence = getLastSection().getSequence() + 1;
        Section section = new Section(line, upStation, downStation, sequence);
        sections.add(section);
    }

    public void validate(Station upStation, Station downStation) {
        if (isAlreadyRegisteredDownStation(downStation)) {
            throw new BusinessException("이미 등록된 역을 하행역으로 가지면 구간을 추가할 수 없습니다. 하행역ID: " + downStation.getId());
        }

        if (!DoesMatchUpStationWithDownEndStation(upStation)) {
            throw new BusinessException("하행 종점이 아닌 역을 상행역으로 가지면 구간을 추가할 수 없습니다. 상행역ID: " + upStation.getId());
        }
    }

    public List<Station> getStations() {
        return toStations();
    }

    public List<Section> getSections() {
        return this.sections;
    }

    private List<Station> toStations() {
        return Stream.concat(Stream.of(this.sections.get(0).getUpStation()),
                this.sections.stream().map(Section::getDownStation))
            .collect(Collectors.toList());
    }

    private boolean isAlreadyRegisteredDownStation(Station downStation) {
        if (isAlreadyRegisteredInFirstUpStation(downStation)) {
            return true;
        }

        return this.sections.stream()
            .anyMatch(localSection -> localSection.getDownStation().getId()
                .equals(downStation.getId()));
    }

    private boolean isAlreadyRegisteredInFirstUpStation(Station downStation) {
        return this.sections.stream().findFirst().orElseThrow().getUpStation().getId()
            .equals(downStation.getId());
    }

    private boolean DoesMatchUpStationWithDownEndStation(Station upStation) {
        return getLastSection().getDownStation().getId()
            .equals(upStation.getId());
    }

    public Section getLastSection() {
        return this.sections.stream()
            .max(Comparator.comparing(Section::getSequence))
            .orElseThrow();
    }
}
