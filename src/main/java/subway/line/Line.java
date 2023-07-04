package subway.line;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    protected Line() {
    }

    public static Line of(Long id, String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Line line = Line.of(name, color, upStationId, downStationId, distance);
        line.id = id;
        return line;
    }

    public static Line of(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Line line = new Line();
        line.name = name;
        line.color = color;
        line.upStationId = upStationId;
        line.downStationId = downStationId;
        line.distance = distance;
        return line;
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

    public Integer getDistance() {
        return distance;
    }
}
