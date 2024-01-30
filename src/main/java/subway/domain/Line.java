package subway.domain;

import subway.exception.SectionAddFailureException;
import subway.exception.SectionDeleteFailureException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/** 지하철 노선 엔티티 */
@Entity
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 지하철 노선명 */
    @Column(length = 20, nullable = false)
    private String name;

    /** 지하철 노선 색 */
    @Column(length = 20, nullable = false)
    private String color;

    @Embedded
    private Sections sections = new Sections();

    protected Line() {}

    private Line(final Long id, final String name, final String color, final Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    private Line(final Long id, final String name, final String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static Line of(final String name, final String color) {
        return new Line(null, name, color);
    }

    public Line update(final String name, final String color) {
        return new Line(id, name, color, sections);
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection(Section section) {
        this.sections.remove(section);
    }

    public void verifyAddableSection(Section section) {
        if(!section.getUpStation().equals(sections.getLastSection().getDownStation())) {
            throw new SectionAddFailureException("새로운 구간의 상행역이 기존 구간의 하행역이 아닙니다.");
        }

        if (sections.isAlreadyExistStation(section.getDownStation())) {
            throw new SectionAddFailureException("새로운 구간의 하행역이 기존 노선에 이미 존재합니다.");
        }
    }

    public void verifyDeletableStation(Station station) {
        if (sections.hasOnlyOneSection()) {
            throw new SectionDeleteFailureException("노선의 구간은 최소 한 개 이상 존재해야 합니다.");
        }

        Section lastSection = sections.getLastSection();
        if (lastSection == null || !lastSection.getDownStation().equals(station)) {
            throw new SectionDeleteFailureException("노선의 하행종점역만 제거할 수 있습니다.");
        }
    }

    public List<Station> getStations() {
        return this.sections.getStations();
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

    public List<Section> getAllSections() {
        return sections.getSections();
    }
}
