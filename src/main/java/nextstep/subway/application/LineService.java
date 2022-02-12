package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {
    private static final String LINE_NAME_IS_ALREADY_REGISTERED = "이미 등록된 노선명입니다.";
    private static final String GIVEN_LINE_ID_IS_NOT_REGISTERED = "등록되지 않은 노선 ID입니다.";
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public LineResponse save(LineRequest request) {
        validate(request.getName());

        Line line = new Line(request.getName(), request.getColor());
        lineRepository.save(line);

        Section section = Section.builder().
                upStation(stationService.findBy(request.getUpStationId()))
                .downStation(stationService.findBy(request.getDownStationId()))
                .line(line)
                .build();

        line.addSection(section);

        return LineResponse.of(line, line.getStations());
    }

    private void validate(String lineName) {
        if (lineRepository.existsByName(lineName)) {
            throw new IllegalArgumentException(LINE_NAME_IS_ALREADY_REGISTERED);
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return LineResponse.lineResponses(lines);
    }

    public LineResponse findLineResponseBy(Long id) {
        Line line = findLineBy(id);
        return LineResponse.of(line);
    }

    public Line findLineBy(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(GIVEN_LINE_ID_IS_NOT_REGISTERED));
    }

    public void updateBy(Long id, LineRequest request) {
        findLineBy(id).update(request.getName(), request.getColor());
    }

    public void deleteBy(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = findLineBy(lineId);
        Station upStation = stationService.findBy(sectionRequest.getUpStationId());
        Station downStation = stationService.findBy(sectionRequest.getDownStationId());
        Section section = sectionRepository.save(new Section(line, upStation, downStation));
        return SectionResponse.of(section);
    }

    public void deleteSectionBy(Long lineId, Long sectionId) {
    }
}
