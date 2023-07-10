package subway.model.line;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import subway.model.section.Section;
import subway.model.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL)
    List<Section> sections;

    @Column(nullable = false)
    private Long distance;

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Station> getStations() {

        List<Station> stations = this.sections.stream()
                                             .map(Section::getUpStation)
                                             .collect(Collectors.toList());
        stations.add(getLastStation());

        return stations;
    }

    private Station getLastStation() {

        if (sections.isEmpty()) {
            throw new IllegalStateException("Line에 포함된 section이 없습니다.");
        }

        return this.sections.get(this.sections.size() - 1)
                            .getDownStation();
    }

    public void addSection(Section newSection) {
        ArrayList<Section> sections = new ArrayList<>(this.sections);
        sections.add(newSection);

        this.sections = sections;
    }

    public boolean isAddableSection(Section newSection) {

        if (!Objects.equals(getLastStation().getId(), newSection.getUpStation().getId())) {
            log.warn("상행역이 노선의 하행종착역과 다릅니다. upStationId: {}", newSection.getUpStation().getId());
            return false;
        }

        if (getStations().stream().anyMatch(it -> Objects.equals(it.getId(), newSection.getDownStation().getId()))) {
            log.warn("하행역이 이미 노선에 포함된 지하철역입니다. stationId: {}", newSection.getDownStation().getId());
            return false;
        }

        return true;
    }

    public boolean isDeletableStation(Station station) {

        if (!Objects.equals(getLastStation().getId(), station.getId())) {
            log.warn("삭제하려는 구간의 정거장이 노선의 마지막 노선이 아닙니다.");
            return false;
        }

        if (this.sections.size() <= 1) {
            log.warn("구간이 1개 뿐인 노선은 삭제할 수 없습니다.");
            return false;
        }

        return true;
    }
}
