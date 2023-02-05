package subway.line;

import java.util.List;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.section.Section;
import subway.section.Sections;
import subway.station.Station;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "LINE")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    @Column(name = "LINE_NAME", length = 20, nullable = false)
    private String name;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "DOWN_STATION_ID")
    private Long downStationId;

    @Column(name = "UP_STATION_ID")
    private Long upStationId;

    @Column(name = "LINE_DISTANCE")
    private Integer lineDistance;

    @Embedded private Sections sections = new Sections();

    public Line(String name, String color, Long downStationId, Long upStationId, Integer distance) {
        this.name = name;
        this.color = color;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.lineDistance = distance;
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public List<Station> getStationList() {
        return sections.getStations();
    }

    public void addSection(Section section) {
        sections.addSections(section);
    }
}
