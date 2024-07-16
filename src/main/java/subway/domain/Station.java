package subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @OneToMany(mappedBy = "upStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sectionAsUpStation;

    @OneToMany(mappedBy = "downStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sectionAsDownStation;

    protected Station() {
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
