package subway.entity;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(Long id, String name, String color) {
        this.id = id;
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

    public List<Section> getSections() {
        return sections;
    }

    public boolean isLastStation(final Long stationId) {
        if (sections.isEmpty() || Objects.isNull(stationId)) {
            return false;
        } else {
            return Objects.requireNonNull(CollectionUtils.lastElement(sections)).getId().equals(stationId);
        }
    }

    public boolean isExistsStation(final Long stationId) {
        for (Section section : sections) {
            if (section.getUpStation().getId().equals(stationId) ||
                    section.getDownStation().getId().equals(stationId)) {
                return true;
            }
        }

        return false;
    }

    public boolean isLastOne() {
        return sections.size() == 1;
    }
}
