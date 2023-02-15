package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LinePatchResponse;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

import subway.model.Line;
import subway.model.Station;
import subway.repository.LineRepository;

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
        return list.stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation));
        return createLineResponse(line);
    }

    public LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(stationService.createStationResponse(line.getUpStation()), stationService.createStationResponse(line.getDownStation())));
    }


    public LineResponse findLineById(Long id) {
        Line line = findVerifiedLine(id);
        return createLineResponse(line);
    }


    private Line findVerifiedLine(Long id) {
        return lineRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 Line 입니다"));
    }


    @Transactional
    public void updateLineById(Long id, LinePatchResponse linePatchResponse) {
        Line line = findVerifiedLine(id);
        line.update(linePatchResponse.getName(), linePatchResponse.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

}
