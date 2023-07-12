package subway.domain;

import lombok.Getter;
import subway.exception.BadRequestSectionsException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DISTANCE")
    private int distance;

    @OneToMany(mappedBy = "line" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();;

    public Line() {
    }

    public Line(String color, String name) {
        this.color = color;
        this.name = name;
    }

    public Line(Long id, String color, String name, int distance, List<Section> sections) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.distance = distance;
        this.sections = sections;
    }

    public void changeColor(String color) {
        if(color != null ){
            this.color = color;
        }
    }

    public void changeName(String name) {
        if(name != null) {
            this.name = name;
        }
    }

    public void addSections(Section sections) {
        isAddableSections(sections);
        this.getSections().add(sections);
    }

    private void isAddableSections(Section newSection) {
        List<Section> sections = this.getSections();
        Station lastStation = sections.get(sections.size() - 1).getDownStation();
        if(!newSection.getUpStation().equals(lastStation)){
            throw new BadRequestSectionsException("구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야합니다.");
        }
        if(sections.stream().anyMatch(section->section.getDownStation().equals(newSection))){
            throw new BadRequestSectionsException("이미 해당 노선에 등록되어있는 역입니다.");
        }
    }

    public void removeSections(Station station) {
        int count = this.getSections().size();
        if (count <= 1) {
            throw new BadRequestSectionsException("노선에는 하나 이상의 구간이 존재해야합니다.");
        }
        if (!this.getSections().get(count- 1).getDownStation().equals(station)) {
            throw new BadRequestSectionsException("해당 노선의 마지막 하행 종점역이 아닙니다.");
        }
    }
}
