package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreateDto;
import nextstep.subway.applicaion.dto.LineDto;
import nextstep.subway.applicaion.dto.LineUpdateDto;
import nextstep.subway.domain.LineStation;
import nextstep.subway.domain.LineStationRepository;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
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
    private final LineStationRepository lineStationRepository;


    @Transactional
    public Long createLine(final LineCreateDto lineDto) {
        Station downStation = stationRepository.findById(lineDto.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("downStation is not found"));

        Station upStation = stationRepository.findById(lineDto.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("upStation is not found"));


        Line line = lineRepository.save(lineDto.toDomain());

        LineStation downLineStation = lineStationRepository.save(LineStation.builder()
                .line(line)
                .station(downStation)
                .build()
        );

        lineStationRepository.save(LineStation.builder()
                .line(line)
                .station(upStation)
                .next(downLineStation)
                .build()
        );


        return line.getId();
    }

    public List<LineDto> getLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(LineDto::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineDto getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("line is not found"));

        return LineDto.of(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateDto lineUpdateDto) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("line is not found"));

        lineRepository.save(lineUpdateDto.toDomain(line));
    }
}
