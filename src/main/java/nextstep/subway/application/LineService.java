package nextstep.subway.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nextstep.subway.exception.LineException.GIVEN_LINE_ID_IS_NOT_REGISTERED;
import static nextstep.subway.exception.LineException.LINE_NAME_IS_ALREADY_REGISTERED;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
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
            throw new BadRequestException(LINE_NAME_IS_ALREADY_REGISTERED);
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
                .orElseThrow(() -> new BadRequestException(GIVEN_LINE_ID_IS_NOT_REGISTERED));
    }

    public void updateBy(Long id, LineRequest request) {
        findLineBy(id).update(request.getName(), request.getColor());
    }

    public void deleteBy(Long id) {
        lineRepository.deleteById(id);
    }
}
