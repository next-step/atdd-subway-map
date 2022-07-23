package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {

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

    public Sections getSections() {
        return sections;
    }

    public void registerSection(Section section) {
        this.sections.add(new Section(section, this));
    }


    private Line(Builder builder) {
        this.name = builder.name;
        this.color = builder.color;
        this.sections = builder.sections;
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }


    public static class Builder {
        private String name;
        private String color;
        private Sections sections;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder Sections(Sections sections) {
            this.sections = sections;
            return this;
        }

        public Line build() {
            return new Line(this);
        }

    }

}
