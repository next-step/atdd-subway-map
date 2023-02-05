package subway.domain.line;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.station.Station;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Color color;

    @Embedded
    private Sections sections;

    public Line(String name, String color) {
        this.name = new Name(name);
        this.color = Color.findByName(color);
        this.sections = new Sections();
    }

    public List<Station> getStationsByAscendingOrder() {
        return sections.getStationsByAscendingOrder();
    }

    public void updateNameAndColor(Line line) {
        this.name = line.getName();
        this.color = line.getColor();
    }
}
