package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        // upStationId나 downStationId의 존재 여부 확인
        List<StationResponse> stations = stationService.findStationsById(
                List.of(lineRequest.getUpStationId(), lineRequest.getDownStationId()));

        Line line = lineRepository.save(
                Line.builder()
                    .name(lineRequest.getName())
                    .color(lineRequest.getColor())
                    .distance(lineRequest.getDistance())
                    .upStationId(lineRequest.getUpStationId())
                    .downStationId(lineRequest.getDownStationId())
                    .build()
        );
        return LineResponse.of(line, stations);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.of(
                        line,
                        stationService.findStationsById(List.of(line.getUpStationId(), line.getDownStationId()))))
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("존재하지 않는 노선입니다. id : %d", id)));
        List<StationResponse> stations = stationService.findStationsById(List.of(line.getUpStationId(), line.getDownStationId()));
        return LineResponse.of(line, stations);
    }

    public LineResponse updateLine(Long id, LineUpdateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("존재하지 않는 노선입니다. id : %d", id)));
        line.update(request);

        List<StationResponse> stations = stationService.findStationsById(List.of(line.getUpStationId(), line.getDownStationId()));
        return LineResponse.of(line, stations);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
