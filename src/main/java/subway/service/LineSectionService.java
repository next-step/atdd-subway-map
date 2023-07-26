package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.LineSectionCreateResponse;
import subway.controller.dto.LineSectionDeleteResponse;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineSectionService {

    private final LineRepository lineRepository;

    private final SectionRepository sectionRepository;

    private final StationRepository stationRepository;

    public LineSectionService(LineRepository lineRepository,
        SectionRepository sectionRepository,
        StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineSectionCreateResponse connectNewSectionIntoLine(Long lineId,
        Long upStationId, Long downStationId,
        Long distance) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new LineNotFoundException("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + lineId));

        Station downStation = stationRepository.findById(downStationId)
            .orElseThrow(() -> new StationNotFoundException(
                "입력된 ID에 해당하는 역이 존재하지 않습니다: " + downStationId));

        Station upStation = stationRepository.findById(upStationId)
            .orElseThrow(() -> new StationNotFoundException(
                "입력된 ID에 해당하는 역이 존재하지 않습니다: " + upStationId));

        line.connectNewSection(upStation, new Section(downStation, distance));

        return LineSectionCreateResponse.responseFrom(line);
    }

    @Transactional
    public LineSectionDeleteResponse disconnectSection(Long lineId, Long stationsId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new LineNotFoundException("입력된 ID에 해당하는 노선이 존재하지 않습니다: " + lineId));

        Station deleteStation = stationRepository.findById(stationsId)
            .orElseThrow(() -> new StationNotFoundException(
                "입력된 ID에 해당하는 역이 존재하지 않습니다: " + stationsId));

        line.disconnectSection(deleteStation);

        return LineSectionDeleteResponse.responseFrom(line);
    }
}
