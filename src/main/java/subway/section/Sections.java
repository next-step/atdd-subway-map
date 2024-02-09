package subway.section;

import subway.exception.SubwayException;
import subway.station.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public void addSection(Section section, Station downStation) {
        if (sections.size() > 0) {
            validateNextSection(section, downStation);
            validateDuplicateStation(section);
        }
        this.sections.add(section);
    }

    private void validateNextSection(Section section, Station downStation) {
        if (!downStation.isEquals(section.getUpStation())) {
            throw new SubwayException("구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닙니다.");
        }
    }

    private void validateDuplicateStation(Section section) {
        if (isContains(section.getDownStation())) {
            throw new SubwayException("이미 등록되어있는 역입니다.");
        }
    }

    private boolean isContains(Station station) {
        return this.sections.stream().anyMatch(section -> section.getUpStation().equals(station));
    }

    public void removeSection(Long stationId, Station downStation) {
        validateLastSection();
        validateEndSection(stationId, downStation);

        Section deleteSection = this.sections.stream()
                .filter(section -> section.getDownStation().isEquals(stationId))
                .findFirst()
                .orElseThrow(() -> new SubwayException("역을 찾을 수 없습니다."));

        this.sections.remove(deleteSection);
    }

    private void validateLastSection() {
        if (sections.size() < 2) {
            throw new SubwayException("구간이 1개인 경우 역을 삭제할 수 없습니다.");
        }
    }

    private void validateEndSection(Long stationId, Station downStation) {
        if (!downStation.isEquals(stationId)) {
            throw new SubwayException("마지막 구간만 제거할 수 있습니다.");
        }
    }

    public List<Station> getOrderedStations(Station upStation) {
        List<Station> stations = new ArrayList<>();
        stations.add(upStation);

        for (int i = 0; i < sections.size(); i++) {
            upStation = findNextStation(upStation);
            stations.add(upStation);
        }

        return stations;
    }

    private Station findNextStation(Station upStation) {
        for (Section section : sections) {
            if (section.getUpStation().equals(upStation)) {
                return section.getDownStation();
            }
        }
        return null;
    }

}
