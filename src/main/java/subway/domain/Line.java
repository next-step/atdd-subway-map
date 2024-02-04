package subway.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int distance;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    public Line() {
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        Long upStationId = upStation.getId();
        Long downStationId = downStation.getId();
        this.upStationId = upStationId;
        this.downStationId = downStationId;

        Section section = new Section(this, upStationId, downStationId, distance);
        sections.add(section);
    }


    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void registSection(Section section) {
        this.downStationId = section.getDownStationId();
        sections.add(section);
    }

    public void deleteSection(Station station) {
        // 지하철 노선에 등록된 역(하행 종점역)만 제거할수 있다.
        if (!downStationId.equals(station.getId())) {
            throw new IllegalArgumentException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있습니다.");
        }
        if (sections.size() == 1) {
            throw new RuntimeException("지하철 구간을 삭제할 수 없습니다");
        }
        sections.remove(sections.stream()
            .filter(section -> section.getDownStationId().equals(station.getId()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("지하철 구간을 삭제할 수 없습니다")));
    }
}
