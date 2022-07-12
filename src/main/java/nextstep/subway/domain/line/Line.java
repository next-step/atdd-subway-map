package nextstep.subway.domain.line;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.station.Station;

@Getter
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String color;

    @NotNull
    @OneToOne
    @JoinColumn(name = "start_section_id")
    private Section startSection;

    public Line() {}

    public Line(String name, String color, Section startSection) {
        this.name = name;
        this.color = color;
        this.startSection = startSection;
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getStations() {
        var stations = new ArrayList<Station>();
        var currentSection = startSection;
        while (currentSection != null) {
            stations.add(currentSection.getStation());
            currentSection = currentSection.getNextSection();
        }
        return stations;
    }
}
