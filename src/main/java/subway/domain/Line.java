package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static subway.common.constants.ErrorConstant.*;
import static subway.common.constants.ErrorConstant.NOT_EXIST_SECTION;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();


    public Section getLastSection() {
        return sections.get(sections.size()-1);
    }

    public void deleteSection(Station station) {
        isDeleteValidation(station);
        sections.remove(getLastSection());
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSections(Section section) {
        sections.add(section);
        section.setLine(this);
    }

    public void isAddValidation(Station upStation, Station downStation) {
        if (!getLastSection().getDownStation().equals(upStation)) {
            throw new IllegalArgumentException(NOT_LAST_STATION);
        }

        if (getLastSection().getUpStation().equals(downStation)) {
            throw new IllegalArgumentException(ALREADY_ENROLL_STATION);
        }
    }


    public void isDeleteValidation(Station station) {
        if (!getLastSection().getDownStation().equals(station)) {
            throw new IllegalArgumentException(NOT_DELETE_LAST_STATION);
        }

        if (sections.size() < 2) {
            throw new IllegalArgumentException(NOT_EXIST_SECTION);
        }
    }

}
