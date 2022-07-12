package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveStationLine(LineRequest lineRequest) {

        Line savedLine = lineRepository.save(new Line(lineRequest));
        return new LineResponse(
                savedLine.getId(), savedLine.getName(), savedLine.getColor(), findStationsRelatedLine(savedLine)
        );
    }

    private List<StationResponse> findStationsRelatedLine(Line savedLine) {
        return stationRepository.findAllById(List.of(savedLine.getUpStationId(), savedLine.getDownStationId()))
                                .stream()
                                .map(StationResponse::of)
                                .collect(Collectors.toList());
    }

    public List<LineResponse> findAllStationsLines() {
        return null;
    }

    public LineResponse findStationLine(Long id) {
        return null;
    }

    @Transactional
    public void updateStationLine(LineUpdateRequest request, Long id) {

    }

    public void deleteStationLine(Long id) {
    }
}
