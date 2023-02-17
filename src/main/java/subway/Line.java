package subway;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20)
    private String color;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Section> sections = new ArrayList<>();
    private Long distance;

    public Line() {
    }

    public Line(String name, String color, List<Section> sections, Long distance) {
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.distance = distance;
    }

    public static Line from(LineRequest lineRequest, Section section) {
        return new Line(lineRequest.getName(),
                lineRequest.getColor(),
                Arrays.asList(section),
                lineRequest.getDistance());
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

    public Long getDistance() {
        return distance;
    }


    public void modify(LineModificationRequest lineModificationRequest) {
        this.name = lineModificationRequest.getName();
        this.color = lineModificationRequest.getColor();
    }

    public List<Station> getStations() {
        return getSections().stream().flatMap(section -> section.getStations().stream())
                .collect(Collectors.toList());
    }

    public void addSection(Section section) {
        getSections().add(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
