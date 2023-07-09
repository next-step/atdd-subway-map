package subway;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    @OneToMany(fetch = FetchType.LAZY)
    private List<Station> stations;

    @Column(nullable = false)
    private Integer distance;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
