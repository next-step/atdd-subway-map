package subway.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.dto.LineSaveRequest;
import subway.line.exception.LineNotFoundException;
import subway.line.service.LineService;
import subway.section.domain.Section;
import subway.section.dto.SectionResponse;
import subway.section.dto.SectionSaveRequest;
import subway.section.repository.SectionCommandRepository;
import subway.section.repository.SectionQueryRepository;
import subway.station.domain.Station;
import subway.station.service.StationService;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionService {

    private final LineService lineService;
    private final StationService stationService;

    @Transactional
    public Section saveSection(Long lineId, SectionSaveRequest saveRequest) {
        Line line = getLine(lineId);
        Station upStation = getStation(saveRequest.getUpStationId());
        Station downStation = getStation(saveRequest.getDownStationId());
        return line.addSection(upStation, downStation, saveRequest.getDistance());
    }

    @Transactional
    public void removeStationById(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        Station station = getStation(stationId);
        line.removeStation(station);
    }

    private Line getLine(Long lineId) {
        return lineService.findLineById(lineId);
    }

    private Station getStation(Long stationId) {
        return stationService.findStationById(stationId);
    }

}