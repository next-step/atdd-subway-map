package subway;

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
    @Embedded
    private StationLink stationLink;
    @Column(nullable = false)
    private Long distance;

    public Line() {
    }

    public Line(String name,
                String color,
                Long upStationId,
                Long downStationId,
                Long distance) {
        this.name = name;
        this.color = color;
        this.stationLink = new StationLink(upStationId, downStationId);
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

    public StationLink getStationLink() {
        return stationLink;
    }

    public Long getDistance() {
        return distance;
    }
}
