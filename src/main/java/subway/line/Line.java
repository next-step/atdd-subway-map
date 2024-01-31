package subway.line;

import subway.station.Station;

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

    public void updateLine(
        LineUpdateRequest request
    ) {
        this.name = request.getName();
        this.color = request.getColor();
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

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
