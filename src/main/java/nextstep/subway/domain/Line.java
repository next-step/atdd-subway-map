package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Integer distance;

    @Embedded
    private Sections sections;

    @Builder
    public Line(final String name, final String color, final Integer distance, final Section section) {
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.sections = new Sections();
        addSection(section);
    }

    public void update(String name, String color) {
        this.name = StringUtils.hasText(name) ? name : this.name;
        this.color = StringUtils.hasText(color) ? color : this.color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
    }
}
