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

    //@OneToMany(mappedBy = "line", cascade = CascadeType.ALL) // section을 각각 persist안해주기 위해
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>(); // 초기화

    // 기본생성자
    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // 생성메서드
    public static Line createLine(String name, String color, Station upStation, Station downStation, int distance) {
        Line line = new Line();
        line.setName(name);
        line.setColor(color);
        line.addSection(upStation, downStation, distance); // 구간(Section)을 바로 안받고 역(Station)으로 구간을 생성
        return line;
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        this.sectionList.add(createSection(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public boolean isContainsStation(Long stationId){
        boolean isUpStation = sectionList.stream()
                                .anyMatch(it -> it.getUpStation().getId() == stationId);
        boolean isDownStation = sectionList.stream()
                                .anyMatch(it -> it.getDownStation().getId() == stationId);
        return isUpStation || isDownStation;
    }

    public int sectionSize(){
        return sectionList == null ? 0 : sectionList.size();
    }

    public Station getLastStation(){
        return sectionList.size() == 0 ? null : sectionList.get(sectionList.size() -1).getDownStation();
    }
    public Station getFirstStation() { return sectionList.size() == 0 ? null : sectionList.get(0).getUpStation(); }

    public Long getLastStationId(){
        return sectionList.size() == 0 ? null : sectionList.get(sectionList.size() -1).getDownStation().getId();
    }
    public Long getFirstStationId() { return sectionList.size() == 0 ? null : sectionList.get(0).getUpStation().getId(); }

    /*public Station getPrevStation(Long stationId){
        return sectionList.stream()
                .filter(it -> it.getDownStation().getId() == stationId)
                .findFirst()
                .get()
                .getUpStation();
    }*/

    public Station getNextStation(Long stationId){
        Station nextStation = null;
        if(sectionList.size() != 0){
            Section section = sectionList.stream()
                                    .filter(it -> it.getUpStation().getId() == stationId)
                                    .findFirst()
                                    .orElse(new Section()); // TODO 널이면 뭐 리턴하지...
            nextStation = section.getDownStation();
        }
        return nextStation;
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
