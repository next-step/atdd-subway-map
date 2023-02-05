package subway.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static subway.common.errorMsgEnum.ERROR_DOWNSTATION_INVAILD_LINE;
import static subway.common.errorMsgEnum.ERROR_UPSTATION_INVAILD_LINE;

@Entity
@Getter
@ToString
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long id;
    private String name;
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

    public void addSection(Section section) throws Exception{
        this.validationSection(section);

        this.sections.add(section);

        if (section.getLine() != this) {
            section.setLine(this);
        }
    }

    private void validationSection(Section section) throws Exception{
        if (! this.isUpStationEqualDownStation(section.getUpStationId())) {
            throw new Exception("["+ERROR_UPSTATION_INVAILD_LINE.getCode()+"]"+ERROR_UPSTATION_INVAILD_LINE.getMsg());
        }

        if (this.alreadyExistsDownStation(section.getDownStationId())) {
            throw new Exception("["+ERROR_DOWNSTATION_INVAILD_LINE.getCode()+"]"+ERROR_DOWNSTATION_INVAILD_LINE.getMsg());
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
