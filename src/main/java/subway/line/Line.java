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

    public Line(Long id, String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.id = id;
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
}
