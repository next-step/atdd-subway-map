package subway.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;
    @NotBlank(message = "name is not blank")
    private String name;
    @NotBlank(message = "color is not blank")
    private String color;
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<Section>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void change(String name, String color){
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);

        if (section.getLine() != this) {
            section.setLine(this);
        }
    }

    public void deleteSection(Section section) {
        this.sections.remove(section);

        if (section.getLine() == this) {
            section.setLine(null);
        }
    }

    public boolean isUpStationEqualDownStation(Long upStationId) {
        if (this.getSections().size() < 1 || this.getSections().get(this.getSections().size() -1 ).getDownStationId().equals(upStationId)) {
            return true;
        }

        return false;
    }

    public boolean alreadyExistsDownStation(Long downStationId) {
        for (Section section : this.getSections()) {
            if (downStationId.equals(section.getUpStationId()) || downStationId.equals(section.getDownStationId())) {
                return true;
            }
        }

        return false;
    }

    public boolean isNotLastSection(Section section) {
        if (!this.sections.get(this.sections.size()-1).equals(section)) {
            return true;
        }

        return false;
    }

    public boolean isNotValidSectionCount() {
        if (this.sections.size() <= 1) {
            return true;
        }

        return false;
    }
}
