package subway.line.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "lines")
@Entity
public class Line {
    private final int CANNOT_DELETE_SECTION_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, mappedBy = "line", orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, Integer distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.sections.add(new Section(distance, upStation, downStation, this));
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void appendSection(Station upStation, Station downStation, Integer distance) {
        if (!validateSectionCreation(upStation, downStation)) {
            throw new IllegalArgumentException("등록할 수 없는 구간입니다");
        }

        Section section = new Section(distance, upStation, downStation, this);
        this.sections.add(section);
    }

    public void removeSection(Station station) {
        if(!validateSectionDeletion(station)) {
            throw new IllegalArgumentException("제거할 수 없는 구간입니다");
        }
        this.sections.remove(this.sections.size() - 1);
    }

    public Station getDownStation() {
        mustContainSection();

        int lastIndex = sections.size() - 1;

        return sections.get(lastIndex).getDownStation();
    }

    private boolean validateSectionCreation(Station newUpStation, Station newDownStation) {
        if(!equalsDownStation(newUpStation)) {
            return false;
        }

        if(alreadyInLine(newDownStation)) {
            return false;
        }

        return true;
    }

    private void mustContainSection() {
        if (sections.isEmpty()) {
            throw new EntityNotFoundException("등록된 구간이 없습니다");
        }
    }

    private boolean equalsDownStation(Station newUpStation) {
        return  getDownStation().equals(newUpStation);
    }

    private boolean alreadyInLine(Station newDownStation) {
        return sections.stream()
                .anyMatch((s) -> s.getUpStation().equals(newDownStation) || s.getDownStation().equals(newDownStation));
    }

    public boolean validateSectionDeletion(Station station) {
        if(!equalsDownStation(station)) {
            return false;
        }

        if(hasSingleSection()) {
            return false;
        }

        return true;
    }

    private boolean hasSingleSection() {
        return getSections().size() == CANNOT_DELETE_SECTION_SIZE;
    }
}
