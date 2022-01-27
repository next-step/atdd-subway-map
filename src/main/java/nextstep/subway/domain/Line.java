package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationResponse;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @Embedded
    private Sections sections = new Sections();

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

    public List<StationResponse> getStationDtoList() {
        return sections.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public void modify(String name, String color) {
        modifyName(name);
        modifyColor(color);
    }

    private void modifyName(String name) {
        if (name != null && !this.name.equals(name)) {
            this.name = name;
        }
    }

    private void modifyColor(String color) {
        if (color != null && !this.color.equals(color)) {
            this.color = color;
        }
    }
}
