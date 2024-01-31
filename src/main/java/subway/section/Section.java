package subway.section;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.Line;
import subway.station.Station;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Line line;

    @ManyToOne
    private Station upstation;

    @ManyToOne
    private Station downstation;

    private int distance;

    public static Section initSection(Line line, Station upstation, Station downstation) {
        Section section = Section.builder()
                .line(line)
                .upstation(upstation)
                .downstation(downstation)
                .distance(line.getDistance())
                .build();

        //sectionRepository.save(section);
        return section;
    }
}
