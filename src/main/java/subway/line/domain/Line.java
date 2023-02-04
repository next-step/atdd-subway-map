package subway.line.domain;

import subway.line.application.LineValidator;
import subway.section.domain.Section;
import subway.section.domain.Sections;
import subway.station.domain.Station;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long distance;

    @OneToOne
    private Station upStation;

    @OneToOne
    private Station downStation;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(String name, String color, Long distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Line update(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }

    public Line addSection(
            Station upStation,
            Station downStation,
            long distance
    ) {
        Section section = new Section(id, upStation, downStation, distance);
        if (LineValidator.isValidate(this, section)) {
            sections.addSection(section);
        }
        return this;
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

    public Long getDistance() {
        return distance;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Sections getSections() {
        return sections;
    }
}
