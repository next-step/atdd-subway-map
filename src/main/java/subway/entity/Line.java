package subway.entity;

import javax.persistence.*;

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
    private Integer distance;

    @Embedded
    private Sections sections = new Sections();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "up_staion_id",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
        nullable = false
    )
    private Station upStation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "down_staion_id",
        foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
        nullable = false
    )
    private Station downStation;

    protected Line() {}

    public Line(
        String name,
        String color,
        Integer distance,
        Station upStation,
        Station downStation
    ) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void createSection(Section section) {
        this.sections.createSection(section);
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
        this.downStation = section.getUpStation();
        this.distance = section.getDistance();
    }

    public void removeSection(Long stationId) {
        this.sections.removeSection(stationId);
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

    public Integer getDistance() {
        return distance;
    }

    public Sections getSections() {
        return sections;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
