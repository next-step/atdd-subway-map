package subway.domain.section;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.Line;
import subway.dto.domain.AddSectionStationVo;
import subway.dto.domain.CreateSectionStationVo;
import subway.dto.domain.DeleteSectionStationVo;
import subway.dto.domain.UpAndDownStationsVo;

import javax.persistence.*;
import java.util.Objects;

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
        this.sectionStations = new SectionStations();
    }

    public void createSectionStations(CreateSectionStationVo createSectionStationVo) {
        sectionStations.createSectionStation(createSectionStationVo);
    }

    public void deleteAllSectionStation() {
        sectionStations.deleteAllSectionStation();
    }

    public void deleteSectionStation(DeleteSectionStationVo deleteSectionStationVo) {
        sectionStations.deleteSectionStation(deleteSectionStationVo);
    }

    public void addSectionStations(AddSectionStationVo addSectionStationVo) {
        sectionStations.addSectionStations(addSectionStationVo);
    }

    public UpAndDownStationsVo getUpAndDownStations() {
        return sectionStations.getUpAndDownStations();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return Objects.equals(getId(), section.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
