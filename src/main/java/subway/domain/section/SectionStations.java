package subway.domain.section;

import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.sectionstation.Direction;
import subway.domain.sectionstation.SectionStation;
import subway.domain.station.Station;
import subway.dto.domain.AddSectionStationVo;
import subway.dto.domain.CreateSectionStationVo;
import subway.dto.domain.DeleteSectionStationVo;
import subway.dto.domain.UpAndDownStationsVo;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Embeddable
public class SectionStations {
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionStation> sectionStations = new ArrayList<>();

    public void createSectionStation(CreateSectionStationVo createSectionStationVo) {
        sectionStations.add(createSectionStationVo.getUpSectionStation());
        sectionStations.add(createSectionStationVo.getDownSectionStation());
    }

    public void deleteAllSectionStation() {
        sectionStations.clear();
    }

    public void deleteSectionStation(DeleteSectionStationVo deleteSectionStationVo) {
        sectionStations.remove(deleteSectionStationVo.getUpSectionStation());
        sectionStations.remove(deleteSectionStationVo.getDownSectionStation());
    }

    public void addSectionStations(AddSectionStationVo addSectionStationVo) {
        sectionStations.add(addSectionStationVo.getUpSectionStation());
        sectionStations.add(addSectionStationVo.getDownSectionStation());
    }

    public UpAndDownStationsVo getUpAndDownStations() {
        Station upStation = sectionStations.stream()
                .filter(sectionStation -> sectionStation.getDirection().equals(Direction.UP))
                .findFirst()
                .map(SectionStation::getStation)
                .orElseThrow();
        Station downStation = sectionStations.stream()
                .filter(sectionStation -> sectionStation.getDirection().equals(Direction.DOWN))
                .findFirst()
                .map(SectionStation::getStation)
                .orElseThrow();
        return new UpAndDownStationsVo(upStation, downStation);
    }

}
