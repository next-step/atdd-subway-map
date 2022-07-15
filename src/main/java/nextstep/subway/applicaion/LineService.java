package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.applicaion.dto.LineUpdateDto;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.infra.LineRepository;
import nextstep.subway.infra.StationRepository;
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

        Station upStation = findStation(lineDto.getUpStationId());
        Station downStation = findStation(lineDto.getDownStationId());

        line.addSection(Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .build());

        return LineDto.of(line);
    }

    public List<LineDto> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineDto getLine(Long id) {
        Line line = findLine(id);

        return LineDto.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateDto lineUpdateDto) {
        Line line = findLine(id);
        line.updateNameAndColor(lineUpdateDto.getName(), lineUpdateDto.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = findLine(id);
        lineRepository.delete(line);
    }

    public Line findLine(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("line is not found"));
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("station is not found"));
    }
}
