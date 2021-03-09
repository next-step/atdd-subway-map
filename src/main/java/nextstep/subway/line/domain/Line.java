package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.line.domain.Section.createSection;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL) // section을 각각 persist안해주기 위해
    private List<Section> sectionList = new ArrayList<>(); // 초기화

    // 기본생성자
    protected Line() {
    }

    // 생성메서드
    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = new Line();
        line.setName(name);
        line.setColor(color);
        line.addSection(createSection(line, upStation, downStation, distance)); // 구간(Section)을 바로 안받고 역(Station)으로 구간을 생성
        return line;
    }

    // 연관관계메서드
    private void addSection(Section section) {
        this.sectionList.add(section);
    }

    // TODO 우선 이름, 색만 업데이트
    public void update(String name, String color) {
        this.name = name;
        this.color = color;
        //this.sectionList = sectionList;
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

    public List<Section> getSectionList() {
        return sectionList;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setColor(String color) {
        this.color = color;
    }

    private void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }
}
