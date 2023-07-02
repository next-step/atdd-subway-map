package subway.line.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.StationOnLineResponse;
import subway.station.domain.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());
        lineRepository.save(line);

        StationOnLineResponse upStation = stationRepository.findById(lineRequest.getUpStationId())
                .map(station -> new StationOnLineResponse(station.getId(), station.getName()))
                .orElseThrow(() -> new IllegalArgumentException("station이 없습니다. stationId=" + lineRequest.getUpStationId()));

        StationOnLineResponse downStation = stationRepository.findById(lineRequest.getDownStationId())
                .map(station -> new StationOnLineResponse(station.getId(), station.getName()))
                .orElseThrow(() -> new IllegalArgumentException("station이 없습니다. stationId=" + lineRequest.getDownStationId()));

        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
    }
}
