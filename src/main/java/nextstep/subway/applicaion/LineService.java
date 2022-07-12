package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.applicaion.dto.LineUpdateDto;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.line.Section;
import nextstep.subway.domain.line.SectionRepository;
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
    private final SectionRepository sectionRepository;

    private final StationRepository stationRepository;


    @Transactional
    public long createLine(final LineCreateDto lineDto) {
        Line line = lineRepository.save(lineDto.toDomain());

        Station upStation = findByStation(lineDto.getUpStationId());
        Station downStation = findByStation(lineDto.getDownStationId());

        sectionRepository.save(
                Section.builder()
                        .upStation(upStation)
                        .downStation(downStation)
                        .line(line)
                        .build());

        return line.getId();
    }

    public List<LineDto> getLines() {
        List<Line> lines = lineRepository.findAll();


        return lines.stream()
                .map(LineDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineDto getLine(Long id) {
        Line line = findByLine(id);

        return LineDto.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateDto lineUpdateDto) {
        Line line = findByLine(id);

        lineRepository.save(lineUpdateDto.toDomain(line));
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = findByLine(id);

        List<Section> sections = sectionRepository.findByLine(line);

        sectionRepository.deleteAllInBatch(sections);
        lineRepository.delete(line);
    }

    private Line findByLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("line is not found"));
    }

    private Station findByStation(Long lineDto) {
        return stationRepository.findById(lineDto)
                .orElseThrow(() -> new NotFoundException("station id is not found"));
    }
}
