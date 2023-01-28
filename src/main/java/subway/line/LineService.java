package subway.line;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Station;
import subway.StationResponse;
import subway.StationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private StationService stationService;
    private LineRepository lineRepository;

    public LineService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line line = Line.builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .build();

        Station upStation = stationService.findOneStation(lineRequest.getUpStationId());
        Station downStation = stationService.findOneStation(lineRequest.getDownStationId());

        line.addStation(upStation);
        line.addStation(downStation);

        Line savedLine = this.lineRepository.save(line);

        return createLineResponse(savedLine);
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stationResponses = line.getStations()
                .stream()
                .map((s) -> this.stationService.createStationResponse(s))
                .collect(Collectors.toList());

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(stationResponses)
                .build();
    }
}
