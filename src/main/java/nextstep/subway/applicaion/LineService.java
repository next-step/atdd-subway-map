package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public LineDto createLine(final LineCreateDto lineDto) {
        Line line = lineRepository.save(lineDto.toDomain());

        return LineDto.of(line);
    }

    public List<LineDto> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

}
