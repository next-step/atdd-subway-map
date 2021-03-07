package nextstep.subway.line.domain;

import nextstep.subway.common.BaseEntity;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public Line() {
    }

    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.addSection(section);
    }

    public static Line of(LineRequest request, Section section) {
        return new Line(request.getName(), request.getColor(), section);
    }

    public void update(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
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

    public void addSection(Section section) {
        boolean isPresent = sections.stream()
                .anyMatch(section::equals);

        if (!isPresent) {
            sections.add(section);
            section.updateLine(this);
        }
    }

    public List<StationResponse> getStationsAll() {
        if (sections.isEmpty()) {
            return Arrays.asList();
        }
        List<StationResponse> responses = new ArrayList<>();
        responses.add(StationResponse.of(sections.get(0).getUpStation()));

        sections.forEach(section-> responses.add(StationResponse.of(section.getDownStation())));

        return responses;
    }

    public Section getLastSection() {
        return sections.get(sections.size()-1);
    }
}
