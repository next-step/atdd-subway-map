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

    private Line() {
    }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
    }

    public static Line of(String name, String color) {
        return new Line(name, color);
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
    public boolean hasStation(Station downStation) {
        return sections.hasStation(downStation);
    }

    /* 하행역이랑 같은지 확인 */
    public boolean hasAnyMatchedDownStation(Station station) {
        return sections.hasAnyMatchedDownStation(station);
    }

    /* 구간 목록 조회 */
    public List<Section> getSectionList() {
        return sections.getSectionList();
    }

    /* 구간 삭제 */
    public void deleteSection(Station station) {
        if (sections.hasOneSection()) {
            throw new BusinessException(ErrorCode.REMAINED_SECTION_ONLY_ONE);
        }
        sections.delete(station);
    }

    /* 구간 추가 */
    public void addSection(Section section) {
        sections.add(section);
        section.setLine(this);
    }
}
