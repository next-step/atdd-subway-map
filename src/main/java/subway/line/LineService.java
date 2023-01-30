package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.DtoConverter;
import subway.subway.Station;
import subway.subway.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    private final DtoConverter dtoConverter;

    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, DtoConverter dtoConverter) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.dtoConverter = dtoConverter;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = findStation(lineRequest.getUpStationId());
        Station downStation = findStation(lineRequest.getDownStationId());
        Line line = dtoConverter.of(lineRequest, upStation, downStation);
        Line saveLine = lineRepository.save(line);

        return dtoConverter.of(saveLine);
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 Station Id입니다."));
    }

    public List<LineResponse> findAllLines() {
        List<Line> lineList = lineRepository.findAll();
        return lineList.stream().map(dtoConverter::of).collect(Collectors.toList());
    }
}
