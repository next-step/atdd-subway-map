package subway.section;

import org.springframework.stereotype.Service;
import subway.line.LineQuery;
import subway.line.dto.LineResponse;
import subway.section.dto.SectionDto;
import subway.station.StationQuery;

import javax.transaction.Transactional;

@Service
public class SectionService {

    private LineQuery lineQuery;

    private StationQuery stationQuery;

    public SectionService(LineQuery lineQuery, StationQuery stationQuery) {
        this.lineQuery = lineQuery;
        this.stationQuery = stationQuery;
    }

    @Transactional
    public LineResponse addSection(Long lineId, SectionDto sectionDto) {
        var line = lineQuery.findById(lineId);
        var stations = stationQuery.getStations(sectionDto.getStationIds());

        var lineSection = new Sections(line.getSections());
        var upStation = stations.findById(sectionDto.getUpStationId());
        var downStation = stations.findById(sectionDto.getDownStationId());

        lineSection.checkLastStationEquals(upStation);
        lineSection.checkLineAlreadyContains(downStation);

        return LineResponse.from(
                line.addSection(
                        upStation,
                        downStation,
                        sectionDto.getDistance()
                )
        );
    }

    public void deleteSection(Long lineId, Long stationId) {
        var line = lineQuery.findById(lineId);
        var station = stationQuery.getStation(stationId);

        var lineSection = new Sections(line.getSections());
        lineSection.checkSizeBeforeDelete();
        lineSection.checkLastStationEquals(station);
        line.deleteSection(lineSection.getLastSection());
    }
}
