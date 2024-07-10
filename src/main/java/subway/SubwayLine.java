package subway;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SubwayLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    @OneToMany(mappedBy = "subwayLine")
    private List<Station> stations = new ArrayList<>();

    public SubwayLine(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    protected SubwayLine() {
    }

    public void updateBasicInfo(String name, String color) {
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

    public Long getDistance() {
        return distance;
    }

    public List<Station> getStations() {
        return new ArrayList<>(this.stations);
    }
}
