package subway.section.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import subway.line.domain.Line;
import subway.station.domain.Station;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line;

    @ManyToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    private int distance;

    public static Section firstSection(Line line, Station upStation, Station downStation, Integer distance) {
        return Section.builder()
                      .line(line)
                      .upStation(upStation)
                      .downStation(downStation)
                      .distance(distance)
                      .build();
    }

    public boolean containStation(Long stationId) {
        return Objects.equals(upStation.getId(), stationId) || Objects.equals(downStation.getId(), stationId);
    }

    public void detractFromLine() {
        this.line = null;
    }

}
