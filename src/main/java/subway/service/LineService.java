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
import subway.repository.entity.Section;
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

        Section section = sectionBuild(request, upStation, downStation);

        Line line = lineBuild(request, section);
        lineRepository.save(line);
        return getLineWithStations(line);
    }

    private Line lineBuild(LineRequest request, Section section) {
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .section(section).build();
    }

    private Section sectionBuild(LineRequest request, Station upStation, final Station downStation) {
        return Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
    }

    /**
     * 노선 생성 시, 노선에 포함된 역 목록 조회 편의 메서드
     */
    private LineResponse getLineWithStations(Line line) {
        List<Station> stations = stationService.findByIdIn(line.getStationIds());

        return LineResponse.from(line, stations);
    }

    /**
     * 노선 목록 조회
     */
    public List<LineResponse> getLines() {
        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::getLineWithStations)
                .collect(Collectors.toList());
    }

    /**
     * 노선 단건 조회
     */
    public LineResponse getLine(Long id) {
        final Line line = findLine(id);
        return getLineWithStations(line);
    }

    /**
     * 노선 단건 조회 편의 메서드
     */
    public Line findLine(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new SubwayRuntimeException(SubwayErrorCode.NOT_FOUND_STATION));
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
     * @param id
     * @param sectionRequest
     */
    @Transactional
    public void addSection(Long id, SectionRequest sectionRequest) {
        Station upStation = stationService.find(sectionRequest.getUpStationId());
        Station downStation = stationService.find(sectionRequest.getDownStationId());

        Section section = Section.of(upStation, downStation, sectionRequest.getDistance());

        Line line = findLine(id);

        line.addSection(section);
    }

    /**
     * 노선에 구간 삭제
     * @param id
     * @param stationId
     */
    @Transactional
    public void deleteSection(final Long id, Long stationId) {
        Line line = findLine(id);
        line.delete(stationId);
    }
}
