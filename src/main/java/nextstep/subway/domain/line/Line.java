package nextstep.subway.domain.line;

import nextstep.subway.applicaion.dto.LineRequest;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
    private static final int MINIMUM_NAME_SIZE = 2;
    private static final int MINIMUM_COLOR_SIZE = 4;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    @Embedded
    private Sections sections;

    protected Line() {/*no-op*/}

    public Line(Long id, String name, String color, List<Section> sections) {

        if (name == null || name.isBlank() || name.length() < MINIMUM_NAME_SIZE) {
            throw new IllegalArgumentException("이름이 공백이거나 2글자 이하일 수 없습니다.");
        }

        if (color.isBlank() || color.length() < MINIMUM_COLOR_SIZE) {
            throw new IllegalArgumentException("색이 공백이거나 4글자 이하일 수 없습니다.");
        }

        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = new Sections(sections);
    }

    public Line(String name, String color, List<Section> sections) {
        this(null, name, color, sections);
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

    public void edit(LineRequest lineRequest) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
    }
}
