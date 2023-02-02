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
    @OneToMany(fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color, List<Section> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public void change(String name, String color){
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section){
        this.sections.add(section);
    }

    public boolean isUpStationNotEqualDownStation(Long upStationId) {
        if (this.getSections().get(this.getSections().size() -1 ).getDownStationId().equals(upStationId)) {
            return false;
        }

        return true;
    }

    public boolean alreadyExistsDownStation(Long downStationId) {
        for (Section section : this.getSections()) {
            if (downStationId.equals(section.getUpStationId()) || downStationId.equals(section.getDownStationId())) {
                return true;
            }
        }

        return false;
    }
}
