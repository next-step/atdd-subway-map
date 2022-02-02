package nextstep.subway.domain.line;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.Sections;
import nextstep.subway.domain.station.Station;
import nextstep.subway.handler.error.custom.BusinessException;
import nextstep.subway.handler.error.custom.ErrorCode;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String color;

    @Embedded
    private Sections sections;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    public Line() {
    }

    private Line(String name, String color, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.sections = new Sections();
    }

    public static Line of(String name, String color, Station upStation, Station downStation) {
        return new Line(name, color, upStation, downStation);
    }

    /* Getter */
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStationList() {
        return sections.getAllStations();
    }

    /* toString */
    @Override
    public String toString() {
        return "Line{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    /* 노선 변경하기 */
    public void modify(String name, String color) {
        modifyName(name);
        modifyColor(color);
    }

    private void modifyName(String name) {
        if (name != null && !this.name.equals(name)) {
            this.name = name;
        }
    }

    private void modifyColor(String color) {
        if (color != null && !this.color.equals(color)) {
            this.color = color;
        }
    }

    /* 노선에 역이 존재하는지 확인 */
    public boolean hasStation(Station station) {
        return sections.hasStation(station);
    }

    /* 구간 목록 조회 */
    public List<Section> getSectionList() {
        return sections.getSectionList();
    }

    /* 구간 삭제 */
    public void deleteSection(Section section) {
        if (sections.hasOneSection()) {
            throw new BusinessException(ErrorCode.REMAINED_SECTION_ONLY_ONE);
        }
        sections.delete(section);
    }

    /* 구간 추가 */
    public void addSection(Section section) {
        // 기존하행 == 새로운상행 : 하행 최신화
        if (isDownStationUpdatable(section)) {
            this.downStation = section.getDownStation();
        }
        // 기존상행 == 새로운하행 : 상행 최신화
        if (isUpStationUpdatable(section)) {
            this.upStation = section.getUpStation();
        }
        sections.add(this, section);
    }

    private boolean isUpStationUpdatable(Section section) {
        return section.isDownStation(this.upStation);
    }

    private boolean isDownStationUpdatable(Section section) {
        return section.isUpStation(this.downStation);
    }

    public boolean isDownStation(Station station) {
        return this.downStation.equals(station);
    }
}
