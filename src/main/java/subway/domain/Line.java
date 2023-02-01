package subway.domain;

import subway.common.BaseEntity;

import javax.persistence.*;

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


    @Embedded
    private Sections sections = new Sections();


    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    public Line(String name, String color, Long distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
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

    public Sections getSections() {
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

    public void changeFirstAndLastStation(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public boolean canAddSection(Station station) {

        if (sections.isEmpty()) {
            return this.upStation.equals(station);
        }
        Section lastSection = sections.getLast();
        return lastSection.isDownStation(station) && sections.hasStation(station);
    }

    public boolean canDeleteSection(Section section) {
        if (sections.lessThanTwo()) {
            return false;
        }
        Section lastSection = sections.getLast();
        return lastSection.equals(section);
    }

    public void deleteSection(Section section) {
        this.sections.remove(section);
    }
}
