package subway.line.domain;

import subway.line.dto.LineRequest;
import subway.line.dto.SectionRequest;
import subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String color;

//    @ManyToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "section_id")
    //@Embedded
    @Embedded
    private Sections sections = new Sections();

//    @Column
//    private Long distance;
//
//    @ManyToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "up_station_id")
//    private Station upStation;
//
//
//    @ManyToOne(cascade = CascadeType.PERSIST)
//    @JoinColumn(name = "down_station_id")
//    private Station downStation;

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = new Sections(sections);
    }

//    public Line(String name, String color, Long distance, Station upStation, Station downStation) {
//        this.name = name;
//        this.color = color;
//        this.distance = distance;
//        this.upStation = upStation;
//        this.downStation = downStation;
//    }

    public static Line of(LineRequest request) {
        return new Line(request.getName(), request.getColor());
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
        return sections.getSections();
    }

    //    public Long getDistance() {
//        return distance;
//    }
//
//    public Station getUpStation() {
//        return upStation;
//    }
//
//    public Station getDownStation() {
//        return downStation;
//    }

    public void update(LineRequest request) {
        if(!name.equals(request.getName())) {
            this.name = request.getName();
        }

        if(!color.equals(request.getColor())) {
            this.color = request.getColor();
        }
    }

    public void addSection(Section section) {
        section.addLine(this);
        sections.addSection(section);
    }

    public List<Long> getStationIds() {
        return sections.getStationIds();
    }

    public void deleteSection(Station station) {
        sections.deleteSection(station);
    }
}
