package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.model.Line;
import subway.model.Station;
import subway.repository.LineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public List<LineResponse> findALlLines() {
        List<Line> list = lineRepository.findAll();
        return list.stream()
                .map(this::createLineResponse).collect(Collectors.toList());
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation));
        return createLineResponse(line);
    }


    public LineResponse createLineResponse(Line line) {
        List<StationResponse> list = new ArrayList<>();
        StationResponse upStationResponse = stationService.createStationResponse(line.getUpStation());
        StationResponse downStationResponse = stationService.createStationResponse(line.getDownStation());
        list.add(upStationResponse);
        list.add(downStationResponse);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), list);
    }


}
