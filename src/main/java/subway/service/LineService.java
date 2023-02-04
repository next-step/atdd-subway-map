package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.model.Line;
import subway.model.Station;
import subway.repository.LineRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public List<LineResponse> findALlLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse).collect(Collectors.toList());
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        List<Station> stations = Arrays.asList(upStation, downStation);
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), stations));
        return createLineResponse(line);
    }


    public LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(),line.getName(), line.getColor(), line.getStations().stream().map(stationService::createStationResponse).collect(Collectors.toList()));
    }

}
