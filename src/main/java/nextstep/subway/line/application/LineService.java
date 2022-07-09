package nextstep.subway.line.application;

import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.station.applicaion.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse createLine(final LineRequest lineRequest) {
        final Line savedLine = lineRepository.save(lineRequest.toLine());
        return LineResponse.builder()
                .id(savedLine.getId())
                .name(savedLine.getName())
                .color(savedLine.getColor())
                .stations(stationService.findStations(Arrays.asList(lineRequest.getUpStationId(), lineRequest.getDownStationId()))).build();
    }

}
