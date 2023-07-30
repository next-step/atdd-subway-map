package subway;

import javax.persistence.*;
import java.util.List;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "station")
    private List<LineStation> lineStations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public void setLineStations(List<LineStation> lineStations) {
        this.lineStations = lineStations;
    }

    public Station() {
    }

    public Station(Long id, String name, List<LineStation> lineStations) {
        this.id = id;
        this.name = name;
        this.lineStations = lineStations;
    }

    public Station(String name) {
        this.name = name;
    }
}
