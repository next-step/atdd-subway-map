package subway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.request.LineRequest;
import subway.controller.response.LineResponse;
import subway.controller.response.StationResponse;
import subway.exception.SubwayException;
import subway.exception.message.SubwayErrorCode;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.repository.entity.Line;
import subway.repository.entity.Station;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    /**
     * 노선 생성
     */
    @Transactional
    public LineResponse createLine(LineRequest req) {
        Line line = lineRepository.save(req.toEntity());
        return getStationsInLine(line);
    }

    /**
     * 노선 생성 시, 노선에 포함된 역 목록 조회 편의 메서드
     */
    private LineResponse getStationsInLine(Line stationLine) {
        List<Station> stations = stationRepository.findAllByIdIn(List.of(stationLine.getDownStationId(), stationLine.getUpStationId()));
        return LineResponse.from(stationLine, stations.stream().map(StationResponse::from).collect(toList()));
    }

    /**
     * 노선 목록 조회
     */
    public List<LineResponse> getLines() {
        final List<Line> lines = lineRepository.findAll();
        return lines.stream().map(this::getStationsInLine).collect(Collectors.toList());
    }

    /**
     * 노선 단건 조회
     */
    public LineResponse getLine(Long id) {
        final Line line = findLine(id);
        return getStationsInLine(line);
    }

    /**
     * 노선 단건 조회 편의 메서드
     */
    public Line findLine(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new SubwayException(SubwayErrorCode.NOT_FOUND_STATION));
    }

    /**
     * 노선 수정
     */
    @Transactional
    public void updateLine(Long id, final LineRequest lineRequest) {
        Line line = findLine(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    /**
     * 노선 삭제
     */
    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
