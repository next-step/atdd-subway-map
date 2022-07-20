package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveStationLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());

        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
        Line savedLine = lineRepository.save(line);
        return new LineResponse(
                savedLine.getId(), savedLine.getName(), savedLine.getColor(), savedLine.getStations()
        );
    }

    public List<LineResponse> findAllStationsLines() {
        return lineRepository.findAll()
                            .stream()
                            .map(line -> new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStations()))
                            .collect(Collectors.toList());
    }

    public LineResponse findStationLine(Long id) {
        Line line = getLine(id);
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStations());
    }

    @Transactional
    public void updateStationLine(LineUpdateRequest request, Long id) {
        Line line = getLine(id);

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    public void deleteStationLine(Long id) {
        Line line = getLine(id);
        lineRepository.deleteById(line.getId());
    }

    @Transactional
    public LineResponse addSection(Long id, SectionRequest sectionRequest) {
        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());
        validSameReqUpStationAndReqDownStation(upStation, downStation);

        Line line = getLine(id);
        line.validSameAlreadyExistDownStationAndReqUpStation(upStation);
        line.addSection(line, upStation, downStation, sectionRequest.getDistance());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStations());
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 역은 없습니다."));
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                             .orElseThrow(() -> new IllegalArgumentException("해당 노선은 없습니다."));
    }

    public void validSameReqUpStationAndReqDownStation(Station upStation, Station downStation) {
        if (Objects.equals(upStation.getName(), downStation.getName()))
            throw new IllegalArgumentException("상행역종점역과 하행역종점역은 같을 수 없습니다.");
    }
}
