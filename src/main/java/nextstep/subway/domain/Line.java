package nextstep.subway.domain;

import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color) {
        validateBlankName(name);
        validateBlankColor(color);
        this.name = name;
        this.color = color;
    }

    private void validateBlankName(final String name) {
        if (Strings.isBlank(name)) {
            throw new IllegalArgumentException("blank line name occurred");
        }
    }

    private void validateBlankColor(final String color) {
        if (Strings.isBlank(color)) {
            throw new IllegalArgumentException("blank line color occurred");
        }
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

    public void update(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
}
