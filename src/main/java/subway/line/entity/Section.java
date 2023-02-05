package subway.line.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Table(name = "sections")
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id")
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    public Section() {
    }

    public Section(Integer distance, Station upStation, Station downStation) {
        this.distance = distance;
        this.upStation = upStation;
        this.downStation = downStation;
    }
}
