package nextstep.subway.domain.line;

import nextstep.subway.domain.section.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany
    private List<Section> sections = new ArrayList<>();

    private Long distance;

    public Line() {
    }

    public Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.sections.add(builder.section);
        this.distance = builder.distance;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Long id, Section section) {
        this.sections.add(section);

        this.distance += section.getDistance();
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
        return Collections.unmodifiableList(sections);
    }

    public Long getDistance() {
        return distance;
    }

    public static class Builder {
        private String name;
        private String color;
        private Section section;
        private Long distance;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder color(String val) {
            color = val;
            return this;
        }

        public Builder sections(Section val) {
            section = val;
            return this;
        }

        public Builder distance(Long val) {
            distance = val;
            return this;
        }

        public Line build() {
            return new Line(this);
        }
    }
}
