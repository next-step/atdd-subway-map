package subway.line.entity;

import lombok.Getter;
import subway.line.dto.request.LineRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "lines")
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "line_sections", joinColumns = @JoinColumn(name = "line_id"), inverseJoinColumns = @JoinColumn(name = "section_id"))
    private final List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(LineRequest lineRequest, Station upStation, Station downStation) {
        this.name = lineRequest.getName();
        this.color = lineRequest.getColor();
        this.sections.add(new Section(lineRequest.getDistance(), upStation, downStation));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void appendSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection() {
        this.sections.remove(this.sections.size() - 1);
    }

    public Station getUpStation() {
        mustContainSection();

        return sections.get(0).getUpStation();
    }

    public Station getDownStation() {
        mustContainSection();

        return sections.get(sections.size() - 1).getDownStation();
    }

    private void mustContainSection() {
        if (sections.isEmpty()) {
            throw new EntityNotFoundException("등록된 구간이 없습니다");
        }
    }
}
