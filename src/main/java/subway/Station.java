package subway;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "upStation")
    private List<Line> linesMappedToUp = new ArrayList<>();

    @OneToMany(mappedBy = "downStation")
    private List<Line> linesMappedToDown = new ArrayList<>();

    public Station() {
    }

    public Station(Long id, String name, List<Line> linesMappedToUp, List<Line> linesMappedToDown) {
        this.id = id;
        this.name = name;
        this.linesMappedToUp = linesMappedToUp;
        this.linesMappedToDown = linesMappedToDown;
    }

    public Station(Long id) {
        this.id = id;
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
