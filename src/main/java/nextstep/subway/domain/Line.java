package nextstep.subway.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

    public Line() {
    }

    public Line(Long id, String name, String color, Sections sections) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
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

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void deleteLastSection(Station foundStation) {
        this.sections.deleteLastSection(foundStation);
    }

    public void addSection(Station upStation, Station downStation, Integer distance) {
        this.sections.addSection(this, upStation, downStation, distance);
    }


    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private Sections sections = new Sections();
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder sections(Sections sections) {
            this.sections = sections;
            return this;
        }

        public Builder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public Line build() {
            try {
                return new Line(id, name, color, sections);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Cannot create Line");
            }
        }
    }
}
