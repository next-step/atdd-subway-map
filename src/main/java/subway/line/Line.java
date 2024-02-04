package subway.line;

import subway.line.section.CannotAddSectionException;
import subway.line.section.CannotDeleteSectionException;
import subway.line.section.Section;
import subway.line.section.SectionRequest;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public Line() {

    }

    public Line(String name, String color) {
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

    public List<Section> getSections() {
        return sections;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }
//
//    public Long addSection(SectionRequest sectionRequest, Station downStation) throws CannotAddSectionException {
//        changeDownStation(sectionRequest.getUpStationId(), downStation);
//        return changeDistance(sectionRequest.getDistance());
//    }
//
//    private void changeDownStation(Long upStationId, Station downStation) throws CannotAddSectionException {
//        if (!isCurrentDownStationId(upStationId)) {
//            throw new CannotAddSectionException("새 구간의 상행역이 현재 노선의 하행 종점역이 아닙니다.");
//        }
//        this.downStation = downStation;
//    }
//
//    private Long changeDistance(Long distance) throws CannotAddSectionException {
//        long increasedDistance = distance - this.distance;
//        if (increasedDistance < 0) {
//            throw new CannotAddSectionException("총 구간 길이가 이전의 길이보다 작습니다.");
//        }
//
//        this.distance = distance;
//        return increasedDistance;
//    }
//
//    public void deleteSection(Section section) throws CannotDeleteSectionException {
//        if (sections.size() == 2) {
//            throw new CannotDeleteSectionException("해당 노선에 상행 종점역과 하행 종점역만 존재합니다.");
//        }
//        if (!downStation.equals(section.getStation())) {
//            throw new CannotDeleteSectionException("삭제 구간이 하행 종점역이 아닙니다.");
//        }
//        sections.remove(sections.size() - 1);
//        this.downStation = sections.get(sections.size() - 1).getStation();
//    }
}
