package nextstep.subway.domain.line;

import nextstep.subway.domain.BaseEntity;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.Sections;
import nextstep.subway.domain.station.Station;
import nextstep.subway.handler.error.custom.BusinessException;
import nextstep.subway.handler.error.custom.ErrorCode;
import nextstep.subway.handler.validator.SectionValidator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections;

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
        this.sections = new Sections();
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

    public List<Station> getStationList() {
        return sections.getAllStations();
    }

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

    public boolean hasStation(Station downStation) {
        return sections.hasStation(downStation);
    }

    public boolean hasAnyMatchedDownStation(Station station) {
        return sections.hasAnyMatchedDownStation(station);
    }

    public boolean isEmptySections() {
        return sections.isEmpty();
    }

    public Sections getSections() {
        return this.sections;
    }

    public List<Section> getSectionList() {
        return this.sections.getSectionList();
    }

    public void deleteSection(Station station) {
        if (sections.hasOneSection()) {
            throw new BusinessException(ErrorCode.REMAINED_SECTION_ONLY_ONE);
        }
        sections.delete(station);
    }

    public void addSection(Section section) {
        sections.add(section);
        section.setLine(this);
    }

    public void validateSection(Station upStation, Station downStation, int distance) {
        SectionValidator.proper(this, upStation, downStation, distance);
    }
}
