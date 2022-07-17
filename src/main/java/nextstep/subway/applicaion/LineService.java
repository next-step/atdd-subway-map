package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.mapper.domain.LineMapper;
import nextstep.subway.applicaion.mapper.response.LineResponseMapper;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {

    private static final String NOT_FOUND_LINE_MESSAGE = "%d번 노선을 찾지 못했습니다.";

    private final LineRepository lineRepository;
    private final LineMapper lineMapper;
    private final LineResponseMapper lineResponseMapper;

    private final SectionService sectionService;

    @Transactional
    public LineResponse createLine(LineRequest lineRequest) {
        Line line = lineRepository.findByNameAndColor(lineRequest.getName(), lineRequest.getColor())
                .orElseGet(() -> lineMapper.map(lineRequest));
        Line savedLine = lineRepository.save(line);
        createSection(savedLine.getId(), lineRequest);

        return lineResponseMapper.map(savedLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(lineResponseMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id)
                .map(lineResponseMapper::map)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_LINE_MESSAGE, id)));
    }

    @Transactional
    public void modifyLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_LINE_MESSAGE, id)));
        line.modifyNameAndColor(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void createSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_LINE_MESSAGE, id)));
        sectionService.createSection(line, sectionRequest);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND_LINE_MESSAGE, id)));
        sectionService.deleteSection(line, stationId);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsById(Long id) {
        return sectionService.findSectionsByLineId(id);
    }
}
