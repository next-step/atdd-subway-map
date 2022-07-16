package nextstep.subway.domain.line;

import java.util.Collections;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
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

    public Line() {}

    public Line(String name, String color, Long upStationId, Long downStationId) {
        this.name = name;
        this.color = color;
    }

    public void changeNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Long upStationId, Long downStationId, Long distance) {
    }

    public void deleteSection(Long stationId) {

    }

    public List<Station> getStations() {
        return Collections.emptyList();
    }
}
