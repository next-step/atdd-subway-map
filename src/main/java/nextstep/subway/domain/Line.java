package nextstep.subway.domain;

import nextstep.subway.exception.RegistrationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line",
               cascade = {CascadeType.PERSIST, CascadeType.MERGE},
               orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
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

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void registSection(Section section) throws Exception {
        Section lastSection = this.sections.stream().reduce((first, second) -> second).orElse(null);
        if(lastSection != null &&
                !lastSection.getDownStation().equals(section.getUpStation())) {
            throw new RegistrationException("새로운 구간이 해당 노선의 하행 종점역이 아닙니다.");
        }
        this.sections.add(section);
    }

}
