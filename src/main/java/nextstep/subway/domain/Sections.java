package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Sections() {
        
    }

    public void addSection(Line line, Station upStation, Station downStation, int distance) {
        validateStationInSection(downStation, upStation);
        this.sections.add(new Section(line, upStation, downStation, distance));
    }

    public void validateStationInSection(Station downStation, Station upStation) {
        if (!this.sections.isEmpty()) {
            checkUpStation(upStation);
            checkDownStation(downStation);
        }
    }

    public Station getFirstStation() {
        final Section firstSection = this.getFirstSection();
        return firstSection.getUpStation();
    }

    public Section getFirstSection() {
        final int firstIndex = 0;
        return this.sections.get(firstIndex);
    }

    public Section getLastSection() {
        final int lastIndex = this.sections.size() - 1;
        return this.sections.get(lastIndex);
    }

    public List<Section> sections() {
        return this.sections;
    }

    private void checkUpStation(Station upStation) {
        final Section lastSection = getLastSection();
        if (!upStation.equals(lastSection.getDownStation())) {
            throw new IllegalArgumentException("등록할 상행종점역은 노선의 하행종점역이어야 합니다.");
        }
    }

    private void checkDownStation(Station downStation) {
        final List<Station> stations = this.sections.stream()
                .flatMap(section -> section.getAllStation().stream())
                .distinct()
                .collect(Collectors.toList());
        if (stations.contains(downStation)) {
            throw new IllegalArgumentException("등록할 하행종점역은 노선에 등록되지 않은 역만 가능합니다.");
        }
    }

    public void deleteLastSection(Station station) {
        if (this.sections.size() <= 1) {
            throw new IllegalArgumentException("지하철 구간이 1개인 경우 구간을 제거할 수 없습니다.");
        }

        final Section lastSection = getLastSection();
        if (!lastSection.getDownStation().equals(station)) {
            throw new IllegalArgumentException("노선에 등록된 역(하행종점역)만 제거 가능합니다.");
        }

        this.sections.remove(lastSection);
    }
}
