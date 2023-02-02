package subway.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public static Line create(String name, String color) {
        return new Line(name, color);
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

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.initLine(this);
    }

    public Station getDownEndStation() {
        return this.sections.get(this.sections.size() - 1).getDownStation();
    }

    public List<Station> getAllStations() {
        return this.getSections().stream()
            .map(s -> List.of(s.getUpStation(), s.getDownStation()))
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    public void deleteSection(Station downStation) {
        checkIsLastSection(downStation);
        checkIsUniqueSection();
        Section section = sections.stream()
            .filter(s -> s.getDownStation().equals(downStation))
            .findAny()
            .orElseThrow(() -> new NoSuchElementException("해당 구간이 존재하지 않습니다."));
        this.sections.remove(section);
    }

    private void checkIsUniqueSection() {
        if (sections.size() <= 1) throw new IllegalStateException("구간이 1개 남은 경우 제거할 수 없습니다.");
    }

    private void checkIsLastSection(Station downStation) {
        if (!getDownEndStation().equals(downStation)) throw new IllegalStateException("마지막이 아닌 구간은 제거할 수 없습니다.");
    }
}
