package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public Line createLine(LineCreateRequest lineCreateRequest) {
        Station downStation = stationRepository.findById(lineCreateRequest.getDownStationId())
                .orElseThrow(NoSuchElementException::new);
        Station upStation = stationRepository.findById(lineCreateRequest.getUpStationId())
                .orElseThrow(NoSuchElementException::new);

        Line line = Line.builder()
                .color(lineCreateRequest.getColor())
                .name(lineCreateRequest.getName())
                .stations(Arrays.asList(downStation, upStation))
                .distance(lineCreateRequest.getDistance())
                .build();

        return lineRepository.save(line);
    }

    public List<Line> getLines() {
        return lineRepository.findAll();
    }

    public Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void updateLine(Long id, LineChangeRequest lineChangeRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        line.updateColor(lineChangeRequest.getColor());
        line.updateName(lineChangeRequest.getName());
    }
}
