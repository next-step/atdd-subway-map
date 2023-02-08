package subway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.request.LineRequest;
import subway.controller.request.SectionRequest;
import subway.controller.response.LineResponse;
import subway.exception.SubwayRuntimeException;
import subway.exception.message.SubwayErrorCode;
import subway.repository.LineRepository;
import subway.repository.entity.Line;
import subway.repository.entity.Station;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    private final StationService stationService;

    /**
     * 노선 생성
     */
    @Transactional
    public LineResponse create(LineRequest request) {
        Station upStation = stationService.find(request.getUpStationId());
        Station downStation = stationService.find(request.getDownStationId());

        Line line = lineRepository.save(request.toEntity());
        line.addSection(upStation, downStation, request.getDistance());
        return LineResponse.from(line);
    }

    /**
     * 노선 목록 조회
     */
    public List<LineResponse> getLines() {
        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 노선 단건 조회
     */
    public LineResponse getLine(Long id) {
        final Line line = findLine(id);
        return LineResponse.from(line);
    }

    /**
     * 노선 단건 조회 편의 메서드
     */
    public Line findLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new SubwayRuntimeException(SubwayErrorCode.NOT_FOUND_STATION));
    }

    /**
     * 노선 수정
     */
    @Transactional
    public void update(Long id, final LineRequest lineRequest) {
        Line line = findLine(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    /**
     * 노선 삭제
     */
    @Transactional
    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

    /**
     * 노선에 구간 등록
     *
     * @param id
     * @param sectionRequest
     */
    @Transactional
    public void addSection(Long id, SectionRequest sectionRequest) {
        Station upStation = stationService.find(sectionRequest.getUpStationId());
        Station downStation = stationService.find(sectionRequest.getDownStationId());

        Line line = findLine(id);
        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }

    /**
     * 노선에 구간 삭제
     *
     * @param id
     * @param stationId
     */
    @Transactional
    public void deleteSection(final Long id, Long stationId) {
        Line line = findLine(id);
        line.delete(stationId);
    }
}
