package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.exceptions.*;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.NotFoundStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new ExistingLineException();
        }
        Station upStation = getStation(request.getUpStationId());
        Station downStation = getStation(request.getDownStationId());
        return createLineResponse(
            lineRepository.save(
                new Line(
                    request.getName(),
                    request.getColor(),
                    upStation,
                    downStation,
                    request.getDistance()
                )
            )
        );
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                    .stream()
                    .map(this::createLineResponse)
                    .collect(Collectors.toList());
    }

    public LineResponse findLine(long id) {
        return lineRepository.findById(id)
                    .map(this::createLineResponse)
                    .orElseThrow(NotFoundLineException::new);
    }

    public LineResponse updateLine(LineRequest request, long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(NotFoundLineException::new);
        line.update(request.toLine());
        return createLineResponse(line);
    }

    public void removeLine(long id) {
        Line line = lineRepository.findById(id)
                                  .orElseThrow(NotFoundLineException::new);

        lineRepository.delete(line);
    }

    public void addLineStation(Long lineId, SectionRequest request) {
        final Line line = getLine(lineId);
        final Station upStation = getStation(request.getUpStationId());
        final Station downStation = getStation(request.getDownStationId());
        addLineStation(line, upStation, downStation, request.getDistance());
    }

    public void addLineStation(Line line, Station upStation, Station downStation, int distance) {
        List<Station> stations = getStations(line);

        if (stations.size() == 0) {
            line.getSections().add(new Section(distance, line, upStation, downStation));
            return;
        }

        boolean isNotValidUpStation = !stations.get(stations.size() - 1)
                                               .getId()
                                               .equals(upStation.getId());
        if (isNotValidUpStation) {
            throw new IsNotValidUpStationException();
        }

        boolean isDownStationExisted = stations.stream()
                                               .anyMatch(it -> it.getId().equals(downStation.getId()));

        if (isDownStationExisted) {
            throw new IsDownStationExistedException();
        }

        line.getSections().add(new Section(distance, line, upStation, downStation));
    }

    public void removeLineStation(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        removeLineStation(line, stationId);
    }

    public void removeLineStation(Line line, Long stationId) {
        if (line.getSections().size() <= 1) {
            throw new NotFoundSectionException();
        }
        final List<Station> stations = getStations(line);
        final boolean isNotValidUpStation = !stations.get(stations.size() - 1)
                                                     .getId()
                                                     .equals(stationId);

        if (isNotValidUpStation) {
            throw new IsNotValidUpStationException("하행 종점역만 삭제가 가능합니다.");
        }

        line.getSections().stream()
            .filter(it -> it.getDownStation().getId().equals(stationId))
            .findFirst()
            .ifPresent(it -> line.getSections().remove(it));
    }

    public List<Station> getStations(Line line) {
        if (line.getSections().isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = new ArrayList<>();
        Station downStation = findUpStation(line);
        stations.add(downStation);

        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream()
                .filter(it -> it.getUpStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getDownStation();
            stations.add(downStation);
        }

        return stations;
    }

    private Station findUpStation(Line line) {
        Station downStation = line.getSections().get(0).getUpStation();
        while (downStation != null) {
            Station finalDownStation = downStation;
            Optional<Section> nextLineStation = line.getSections().stream()
                .filter(it -> it.getDownStation() == finalDownStation)
                .findFirst();
            if (!nextLineStation.isPresent()) {
                break;
            }
            downStation = nextLineStation.get().getUpStation();
        }

        return downStation;
    }

    public LineResponse createLineResponse(Line line) {
        return new LineResponse(
            line.getId(),
            line.getName(),
            line.getColor(),
            getStations(line)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList()),
            line.getCreatedDate(),
            line.getModifiedDate()
        );
    }


    private Line getLine(long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(NotFoundLineException::new);
    }

    private Station getStation(long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(NotFoundStationException::new);
    }

}
