package subway.line;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 30, nullable = false)
    private String color;

    private Long upStationId;

    private Long downStationId;

    //우선 단위는 km라고 생각.
    private Long distance;

    public Line() {
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public void updateLine(String name, String color){
        this.name = name;
        this.color = color;
    }

    public static Line createLine(String name, String color, Long upStationId, Long downStationId, Long distance){
        Line newLine = new Line(name, color, upStationId, downStationId, distance);
        return newLine;
    }

}
