package nextstep.subway.line.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class LineStations {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
    List<LineStation> lineStation = new ArrayList<>();

    public void add(LineStation lineStation) {
        this.lineStation.add(lineStation);
    }
}
