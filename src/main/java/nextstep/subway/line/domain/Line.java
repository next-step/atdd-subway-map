package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public void setUpdate(final Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }

    public void addSection(final Section section) {
        // TODO : Validate 추가
        // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
        // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        sections.add(section);
    }

    public void addSection(final Long upStationId, final Long downStationId, final Long distance) {
        addSection(new Section(this, upStationId, downStationId, distance));
    }

    public void deleteStation(Station deleteStation) {
        // TODO : Validate 추가
        // 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
        // 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
        final int lastStationIndex  = sections.size() - 1;
        sections.remove(lastStationIndex);
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

    public List<Section> getSections() {
        return sections;
    }
}
