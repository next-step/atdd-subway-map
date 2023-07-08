package subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.section.domain.Section;
import subway.station.domain.Station;

/**
 * - 노선은 "여러 개의 순서가 있는" 역을 가질 수 있다.
 * - 노선과 역은 다대다 관계이다.
 * - 이를 풀어내기 위해서는 중간 테이블이 필요한데, 이를 구간으로 표현할 수 있을 것이다.
 * - 구간은 노선, 상행역, 하행역, 역간 거리를 정보로 가진다.
 * - 노선 1개에는 N개의 구간이 속할 수 있다. (노션 1개는 N개의 구간을 가질 수 있다.)
 * - 역 1개에는 N개의 구간이 속할 수 있다. (역 1개에는 N개의 구간을 가질 수 있다.)
 * - N:1을 생각할 때는 사람과 그룹을 생각하자. 그룹 1개에는 N명의 사람을 가질 수 있다.
 */
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return sections.stream()
                .flatMap(section -> section.getStations().stream())
                .collect(Collectors.toList());
    }
}
