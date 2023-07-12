package subway.rds_module.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subway_lines")
public class SubwayLineJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subway_line_id")
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long startSectionId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="subway_section_id")
    private List<SubwaySectionJpa> subwaySections = new ArrayList<>();

    public SubwayLineJpa() {
    }

    public SubwayLineJpa(Long id, String name, String color, Long startSectionId, List<SubwaySectionJpa> subwaySections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.startSectionId = startSectionId;
        this.subwaySections = subwaySections;
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

    public List<SubwaySectionJpa> getSubwaySections() {
        return subwaySections;
    }

    public boolean isNew() {
        return id == null;
    }
}
