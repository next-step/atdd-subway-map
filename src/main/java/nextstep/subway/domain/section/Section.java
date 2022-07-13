package nextstep.subway.domain.section;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import nextstep.subway.domain.station.Station;

@Table(uniqueConstraints = {
        @UniqueConstraint(name = "line_station_constraint", columnNames = {"lineId", "station_id"})
})
@Getter
@Entity
@ToString
@NoArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long lineId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_section_id")
    private Section prevSection;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_section_id")
    private Section nextSection;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @NotNull
    private Long distance;

    public Section(Long lineId, Station station) {
        this.lineId = lineId;
        this.station = station;
        this.distance = 0L;
    }

    public void setNextSection(Section nextSection, Long distance) {
        this.nextSection = nextSection;
        this.distance = distance;
    }

    public void setPrevSection(Section prevSection) {
        this.prevSection = prevSection;
    }

    public void resetNextSection() {
        this.nextSection = null;
        this.distance = 0L;
    }
}
