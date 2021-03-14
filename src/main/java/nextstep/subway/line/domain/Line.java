package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

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

    @Embedded
    private Sections sections = new Sections();

    // 기본생성자
    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Station upStation, Station downStation, int distance) {
        this.name = name;
        this.color = color;
        this.addSection(upStation, downStation, distance); // 구간(Section)을 바로 안받고 역(Station)으로 구간을 생성
    }

    public void addSection(Station upStation, Station downStation, int distance) {
        this.sections.addSection(createSection(this, upStation, downStation, distance));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     * 지하철 역 업데이트
     */
    public void update(String name, String color, Station upStation, Station downStation, int distance) {
        // 벨리데이션
        validateUpdate(upStation.getId(), downStation.getId());
        // 수정
        this.name = name;
        this.color = color;
        this.addSection(upStation, downStation, distance);
    }

    /**
     * 지하철 역 업데이트 - 벨리데이션
     */
    private void validateUpdate(Long upStationId, Long downStationId){
        if(sections.getSectionSize() != 0 && (sections.getLastStationId() != upStationId)) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.");
        }
        if(sections.isContainsStation(downStationId)) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.");
        }
    }

    /**
     * LineResponse 반환
     */
    public LineResponse getLineResponse(){
        List<StationResponse> stations = new ArrayList<>();

        if(sections.getSectionSize() > 0){
            Long id = sections.getFirstStationId();
            stations.add(StationResponse.of(sections.getFirstStation()));

            Station station = null;
            while ((station = sections.getNextStation(id)) != null){
                id = station.getId();
                stations.add(StationResponse.of(station));
            }
        }
        return new LineResponse(this.id, this.name, this.color, stations, this.getCreatedDate(), this.getModifiedDate());
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

    public Sections getSections() {
        return sections;
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

    private void setSections(Sections Sections) {
        this.sections = sections;
    }
}
