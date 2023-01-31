package subway.domain;

import subway.common.BaseEntity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;


    @OneToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @OneToOne(targetEntity = Station.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;


    @OneToMany(targetEntity = Section.class, fetch = FetchType.LAZY)
    private List<Section> sections;


    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    public Line(String name, String color, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = new LinkedList<>();
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

    public List<Section> getSections() {
        return sections;
    }


    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public void setUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public boolean canAddSection(Station station) {

        if (sections.isEmpty()) {
            return this.upStation.equals(station);
        }

        Section lastSection = sections.get(getSections().size() - 1);
        return lastSection.isDownStation(station);
    }

    public boolean canDeleteSection(Section section) {
        if (this.sections.size() <= 1) {
            return false;
        }
        Section lastSection = this.sections.get(getSections().size() - 1);
        if (lastSection.equals(section)) {
            return true;
        }
        return false;
    }

    public void deleteSection(Section section) {
        this.sections.remove(section);
    }
}
