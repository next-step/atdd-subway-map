package subway.domain.section;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.dto.domain.UpAndDownStationsDto;
import subway.domain.line.Line;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Distance distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineId", referencedColumnName = "id")
    private Line line;

    @Embedded
    private SectionStations sectionStations;

    public Section(Long distance, Line line) {
        this.distance = new Distance(distance);
        this.line = line;
    }

    public UpAndDownStationsDto getUpAndDownStations() {
        return sectionStations.getUpAndDownStations();
    }

}
