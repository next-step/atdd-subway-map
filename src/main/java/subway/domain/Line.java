package subway.domain;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int distance;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Section> sections = Collections.emptySet();

    public Line() {
    }

    public Line(String name, String color, int distance, Long upStationId, Long downStationId) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }


    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void registSection(Section section) {
        sections.add(section);
    }

    private boolean isNotLastSection(Section section) {
        return !section.getUpStationId().equals(downStationId);
    }
}
