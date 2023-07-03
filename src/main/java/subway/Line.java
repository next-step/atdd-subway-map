package subway;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToOne(mappedBy = "id")
    private Station upStation;

    @OneToOne(mappedBy = "id")
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    @OneToMany(mappedBy = "station")
    private List<LineStation> stations;

    public Line() {

    }

    public Line(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
