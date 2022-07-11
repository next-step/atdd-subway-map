package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.applicaion.dto.LineUpdateDto;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;


    @Transactional
    public LineDto createLine(final LineCreateDto lineDto) {
        Line line = lineRepository.save(lineDto.toDomain());

        return convertToLineDto(line);
    }

    public List<LineDto> getLines() {
        List<Line> lines = lineRepository.findAll();


        return lines.stream()
                .map(this::convertToLineDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineDto getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("line is not found"));

        return convertToLineDto(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateDto lineUpdateDto) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("line is not found"));

        lineRepository.save(lineUpdateDto.toDomain(line));
    }

    private LineDto convertToLineDto(Line line) {
        List<Station> stations = stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()));

        return LineDto.of(line, stations);
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("line is not found"));

        lineRepository.delete(line);
    }
}
