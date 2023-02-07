package subway.domain;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SECTION_ID")
    private Long id;

    @Column(name = "LINE_ID")
    private Long lineId;

    @ManyToOne
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;

    @ManyToOne
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;

    @Column(name = "SECTION_DISTANCE")
    private Integer sectionDistance;

    public Section(Long lineId, Station upStation, Station downStation, Integer sectionDistance) {
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.sectionDistance = sectionDistance;
    }
}
