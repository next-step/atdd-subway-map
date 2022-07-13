package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.applicaion.dto.LineUpdateDto;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.service.LineService;
import nextstep.subway.domain.service.SectionService;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.service.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineApiService {

    private final LineService lineService;
    private final SectionService sectionService;
    private final StationService stationService;


    @Transactional
    public long createLine(final LineCreateDto lineDto) {
        Line line = lineService.save(lineDto.toDomain());

        Station upStation = stationService.findStation(lineDto.getUpStationId());
        Station downStation = stationService.findStation(lineDto.getDownStationId());

        sectionService.save(
                Section.builder()
                        .upStation(upStation)
                        .downStation(downStation)
                        .line(line)
                        .build());

        return line.getId();
    }

    public List<LineDto> getLines() {
        List<Line> lines = lineService.findAll();

        return lines.stream()
                .map(LineDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineDto getLine(Long id) {
        Line line = lineService.findLine(id);

        return LineDto.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateDto lineUpdateDto) {
        Line line = lineService.findLine(id);

        lineService.save(lineUpdateDto.toDomain(line));
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = lineService.findLine(id);
        List<Section> sections = sectionService.findByLine(line);

        sectionService.deleteAll(sections);
        lineService.delete(line);
    }
}
