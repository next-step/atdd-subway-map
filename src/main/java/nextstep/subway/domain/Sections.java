package nextstep.subway.domain;

import nextstep.subway.exception.DeleteSectionException;
import nextstep.subway.exception.AddSectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.*;

@Embeddable
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        if (sections.isEmpty()) {
            this.sections.add(section);
            return;
        }

        matchLastStationAndNewUpStation(section.getUpStation());
        duplicateStation(section.getDownStation());

        this.sections.add(section);
    }

    private void matchLastStationAndNewUpStation(Station upStation) {
        Station lastStation = lastSection().getDownStation();

        if (!lastStation.equals(upStation)) {
            throw new AddSectionException("기존 노선의 종점역과 신규 노선의 상행역이 일치하지 않습니다.");
        }
    }

    private void duplicateStation(Station downStation) {
        Optional<Station> findStation = sections.stream()
                .map(Section::getUpStation)
                .filter(upStation -> upStation.equals(downStation))
                .findAny();

        if (findStation.isPresent()) {
            throw new AddSectionException("신규 구간의 하행역이 기존 노선의 역에 이미 등록되어 있습니다.");
        }
    }

    private Section lastSection() {
        return sections.get(sections.size() - 1);
    }

    public List<Station> allStations() {
        List<Station> stations = new ArrayList<>();

        if (sections.isEmpty()) {
            return unmodifiableList(stations);
        }

        stations.add(sections.get(0).getUpStation());

        sections.forEach(section -> {
            stations.add(section.getDownStation());
        });

        return unmodifiableList(stations);
    }

    public void removeSection(Station deleteStation) {
        if (sections.size() == 1) {
            throw new DeleteSectionException("구간이 1개인 노선은 구간 삭제를 진행할 수 없습니다.");
        }

        if (!lastSection().getDownStation().equals(deleteStation)) {
            throw new DeleteSectionException("삭제하려는 역이 노선에 등록되지 않은 역이거나, 마지막 구간의 역이 아닙니다.");
        }

        sections.remove(lastSection());
    }
}
