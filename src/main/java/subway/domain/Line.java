package subway.domain;

import lombok.Getter;

import javax.persistence.*;
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

    @Embedded
    private LineSections sections = new LineSections();

    public Line() {
    }

    public Line(String color, String name) {
        this.color = color;
        this.name = name;
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
        this.sections.add(sections);
    }

    public void removeSections(Station station) {
        this.sections.remove(station);
    }

    public List<Station> getStations() {
        return this.sections.getStations();
    }
}
