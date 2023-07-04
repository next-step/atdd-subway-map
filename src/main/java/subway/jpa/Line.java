package subway.jpa;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(name = "up_station_id", nullable = false)
    private Long upStationId;

    @Column(name = "down_station_id", nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Integer distance;

    public Line() {}

    public Line(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getColor() { return color; }

    public Long getUpStationId() { return upStationId; }

    public Long getDownStationId() { return downStationId; }

    public Integer getDistance() { return distance; }

    public void setId(Long id) { this.id = id; }
}
