package subway.line;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import subway.SubwayService;
import subway.line.domain.Line;
import subway.line.request.LineRequest;
import subway.line.response.LineResponse;
import subway.station.StationRepository;
import subway.station.domain.Station;

@RequiredArgsConstructor
@Service
public class LineService implements SubwayService<LineRequest, LineResponse> {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    @Override
    public LineResponse create(LineRequest lineRequest) {
        return saveLine(lineRequest);
    }

    private LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(
            lineRequest.getName(),
            lineRequest.getColor(),
            lineRequest.getUpStationId(),
            lineRequest.getDownStationId(),
            lineRequest.getDistance()
        ));
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = getStation(line.getUpStationId());
        Station downStation = getStation(line.getDownStationId());

        return LineResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .stations(Arrays.asList(upStation, downStation))
            .build();
    }

    private Station getStation(Long id) {
        return stationRepository.findById(id)
            .orElseGet(() -> stationRepository.save(new Station("지하철역" + id)));
    }
}