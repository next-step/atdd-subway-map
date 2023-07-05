package subway.rds_module.entity;

import subway.subway.domain.SubwaySection;

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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="subway_section_id")
    private List<SubwaySectionJpa> subwaySections = new ArrayList<>();

    public SubwayLineJpa() {
    }

    public SubwayLineJpa(String name, String color, List<SubwaySectionJpa> subwaySections) {
        this.name = name;
        this.color = color;
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
}
