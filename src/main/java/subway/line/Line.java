package subway.line;

import javax.persistence.*;
import java.util.List;

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

    @Column(nullable = false)
    private Long upStationId;
    @Column(nullable = false)
    private Long downStationId;

    public Line() {
    }

    public Line(
            String name, String color, Long distance,
            Long upStationId, Long downStationId
    ) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public List<Long> stationIds() {
        return List.of(upStationId, downStationId);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
