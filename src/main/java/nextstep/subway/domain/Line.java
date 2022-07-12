package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @Column(name = "line_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    protected Line() {
    }

    private Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Line of(String name, String color, Long upStationId, Long downStationId, int distance) {
        return new Line(name, color, upStationId, downStationId, distance);
    }

    public void change(String name, String color) {
        this.name = name;
        this.color = color;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
