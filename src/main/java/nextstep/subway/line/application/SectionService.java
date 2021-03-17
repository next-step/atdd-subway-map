package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class SectionService {

    private StationService stationService;
    private LineRepository lineRepository;

    public SectionService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    public SectionResponse addSection(Long lineId, SectionRequest request) {
        Line line = getLineEntity(lineId);
        Station upStation = stationService.getStationEntity(request.getUpStationId());
        Station downStation = stationService.getStationEntity(request.getDownStationId());

        Section section = line.addSection(upStation, downStation, request.getDistance());
        lineRepository.save(line);
        return SectionResponse.of(section);
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLineEntity(lineId);
        Station station = stationService.getStationEntity(stationId);
        line.deleteLastSection(station);
    }

    private Line getLineEntity(Long lineId) {
        if (Objects.isNull(lineId)) {
            throw new IllegalArgumentException("지하철 노선 ID를 입력해주세요.");
        }
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

}
