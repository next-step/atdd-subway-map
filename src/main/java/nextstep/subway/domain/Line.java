package nextstep.subway.domain;

import nextstep.subway.exception.BadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() { }

    private Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line createLine(String name, String color) {
        return new Line(name, color);
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        sections.add(section);
        section.setLine(this);
    }

    public void deleteSection(Long stationId) {
        Section lastSection = sections.get(getSections().size() - 1);

        validateDelete(stationId);
        sections.remove(lastSection);
    }

    private void validateDelete(Long stationId) {
        Station downEndStation = sections.get(sections.size() - 1).getDownStation();
        if (!downEndStation.getId().equals(stationId)) {
            throw new BadRequestException("구간 삭제는 하행 종점역만 삭제할 수 있습니다.");
        }

        if (getSections().size() == 1) {
            throw new BadRequestException("구간 삭제는 구간이 2개 이상이어야 합니다.");
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

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Objects.equals(getId(), line.getId()) && Objects.equals(getName(), line.getName()) && Objects.equals(getColor(), line.getColor()) && Objects.equals(getSections(), line.getSections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getColor(), getSections());
    }
}
