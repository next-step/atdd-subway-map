package subway.section.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.entity.Line;
import subway.station.entity.Station;

import javax.persistence.*;


@Builder
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @JoinColumn(name = "line_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Line line;

    @JoinColumn(name = "down_station_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Station downStation;

    @JoinColumn(name = "up_station_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Station upStation;

    @Column(length = 20, nullable = false)
    private long distance;

    public void changeLine(Line line) {
        this.line = line;
    }
}
