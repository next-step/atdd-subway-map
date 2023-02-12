package subway.line;

import lombok.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import subway.*;
import subway.line.section.*;

import java.util.*;
import java.util.stream.*;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineResponse createLine(final LineCreateRequest lineCreateRequest) {
        final var upStation = stationService.getById(lineCreateRequest.getUpStationId());
        final var downStation = stationService.getById(lineCreateRequest.getDownStationId());

        final var section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineCreateRequest.getDistance())
                .build();

        final var line = Line.builder()
                .name(lineCreateRequest.getName())
                .color(lineCreateRequest.getColor())
                .section(section)
                .build();

        return LineResponse.from(lineRepository.save(line));
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(final Long lineId) {

        return LineResponse.from(getById(lineId));
    }

    public Line getById(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<LineResponse> getAll() {

        return lineRepository.findAll()
                .stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public void editLine(final Long lineId, final LineEditRequest lineEditRequest) {

        final var line = getById(lineId)
                .change(
                        lineEditRequest.getName(),
                        lineEditRequest.getColor()
                );
        lineRepository.save(line);
    }

    public void deleteById(final Long lineId) {

        lineRepository.deleteById(lineId);
    }
}
