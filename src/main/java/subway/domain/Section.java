package subway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import javax.persistence.*;

@Getter
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "up_station_id")
    private Station upStation;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "down_station_id")
    private Station downStation;
    private int distance;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    @JsonIgnore
    private Line line;

    protected Section() {
    }

    public Section(Station upStation, Station downStation, int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
