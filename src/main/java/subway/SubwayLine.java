package subway;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class SubwayLine {
    @Id
    @GeneratedValue
    private Long id;

    private String color;

    @Column(length=20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "subwayLine")
    private List<Station> stations = new ArrayList<>();

    public SubwayLine() {
    }

    public SubwayLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Station> stations() {
        return stations;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void setName(String name) {
        this.name = name;
    }
}
