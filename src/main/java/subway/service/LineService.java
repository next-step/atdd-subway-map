package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CheckDuplicateStationException;
import subway.exception.InvalidUpStationException;
import subway.exception.NotFoundException;
import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineUpdateRequest;
import subway.controller.dto.LineResponse;
import subway.domain.Section;
import subway.domain.repository.SectionRepository;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.SectionResponse;
import subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

import static subway.controller.dto.LineResponse.lineToLineResponse;
import static subway.controller.dto.SectionResponse.sectionToSectionResponse;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private SectionRepository sectionRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        Station upStation = stationService.getStationById(lineCreateRequest.getUpStationId());
        Station downStation = stationService.getStationById(lineCreateRequest.getDownStationId());

        Line line = Line.builder()
                .name(lineCreateRequest.getName())
                .color(lineCreateRequest.getColor())
                .build();

        line = lineRepository.save(line);

        line.addSection(Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineCreateRequest.getDistance())
                .line(line)
                .build());

        return lineToLineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line findLine = getLineById(id);
        findLine.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line findLine = getLineById(id);
        lineRepository.deleteById(findLine.getId());
    }

    public List<LineResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(LineResponse::lineToLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SectionResponse saveLineSection(Long lineId, SectionCreateRequest request) {
        Station upStation = stationService.getStationById(Long.valueOf(request.getUpStationId()));
        Station downStation = stationService.getStationById(Long.valueOf(request.getDownStationId()));

        Line line = getLineById(lineId);

        validDownStation(Long.valueOf(request.getDownStationId()), line);
        validUpStation(Long.valueOf(request.getUpStationId()), line);

        Section section = sectionRepository.save(Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build());

        return sectionToSectionResponse(section);
    }

    @Transactional
    public void deleteLineSection(Long lineId, Long stationId) {
        Line line = getLineById(lineId);
        line.deleteSection(stationId);
    }

    public LineResponse findLine(Long id) {
        Line findLine = getLineById(id);
        return lineToLineResponse(findLine);
    }

    public Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("지하철 노선이 존재하지 않습니다."));
    }

    private static void validDownStation(Long downStationId, Line line) {
        List<Long> registeredStationIds = line.getStationIds();

        if (registeredStationIds.contains(downStationId)) {
            throw new CheckDuplicateStationException("이미 해당노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }

    private static void validUpStation(Long upStationId, Line line) {
        Long downStationId = line.findDownStationId();

        if (downStationId != upStationId) {
            throw new InvalidUpStationException("새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 합니다.");
        }
    }

}
