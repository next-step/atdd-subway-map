package subway.line.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Table(name = "lines")
@Entity
public class Line {
    final int CANNOT_DELETE_SECTION_SIZE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
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

    public void appendSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection() {
        this.sections.remove(this.sections.size() - 1);
    }

    public Station getDownStation() {
        mustContainSection();

        return sections.get(sections.size() - 1).getDownStation();
    }

    public Boolean validateSectionCreation(Long newUpStationId, Long newDownStationId) {
        if(!equalsDownStation(newUpStationId)) {
            return Boolean.FALSE;
        }

        if(alreadyInLine(newDownStationId)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Boolean validateSectionDeletion(Long stationId) {
        if(!equalsDownStation(stationId)) {
            return Boolean.FALSE;
        }

        if(hasSingleSection()) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private void mustContainSection() {
        if (sections.isEmpty()) {
            throw new EntityNotFoundException("등록된 구간이 없습니다");
        }
    }

    private Boolean equalsDownStation(Long stationId) {
        Long downStationId = this.getDownStation().getId();
        return downStationId.equals(stationId);
    }

    private Boolean alreadyInLine(Long downStationId) {
        return sections.stream()
                .anyMatch((s) -> s.getUpStation().getId().equals(downStationId) ||
                        s.getDownStation().getId().equals(downStationId));
    }

    private Boolean hasSingleSection() {
        return getSections().size() == CANNOT_DELETE_SECTION_SIZE;
    }
}
