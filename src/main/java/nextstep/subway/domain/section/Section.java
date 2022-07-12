package nextstep.subway.domain.section;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.station.Station;

@Getter
@Entity
@NoArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "next_section_id")
    private Section nextSection;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @NotNull
    private Long distance;

    public Section(Station station) {
        this.station = station;
        this.distance = 0L;
    }

    public Section(Station station, Long distance) {
        this.station = station;
        this.distance = distance;
    }

    public void setNextSection(Section nextSection) {
        this.nextSection = nextSection;
    }
}
