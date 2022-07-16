package nextstep.subway.applicaion.line;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionCreationRequest;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineResponse create(LineCreationRequest request) {
        var upStation = getStation(request.getUpStationId());
        var downStation = getStation(request.getDownStationId());
        var line = lineRepository.save(new Line(
                request.getName(),
                request.getColor(),
                upStation,
                downStation,
                request.getDistance()
        ));

        return LineResponse.fromLine(line);
    }

    public LineResponse modify(Long lineId, LineModificationRequest request) {
        var line = getLine(lineId);
        line.changeNameAndColor(request.getName(), request.getColor());

        return LineResponse.fromLine(line);
    }

    public void remove(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public void addSection(Long lineId, SectionCreationRequest request) {
        var line = getLine(lineId);
        var upStation = getStation(request.getUpStationId());
        var downStation = getStation(request.getDownStationId());

        line.addSection(upStation, downStation, request.getDistance());
    }

    public void deleteSection(Long lineId, Long stationId) {
        getLine(lineId).deleteSection(stationId);
    }

    private Line getLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 노선 ID 입니다."));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 역 ID 입니다."));
    }
}
