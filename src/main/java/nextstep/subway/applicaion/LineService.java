package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.exception.unchecked.SectionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse createLine(LineRequest request) {
        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());
        Line savedLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        List<StationResponse> stations = findAllStationInLine(savedLine);
        return LineResponse.of(savedLine, stations);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll()
                .stream()
                .map(line -> LineResponse.of(line, findAllStationInLine(line)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long lineId) {
        Line findLine = findLineById(lineId);
        List<StationResponse> stations = findAllStationInLine(findLine);
        return LineResponse.of(findLine, stations);
    }

    public void editLine(Long lineId, LineRequest request) {
        Line findLine = findLineById(lineId);
        findLine.edit(request.getName(), request.getColor(), request.getDistance());
    }

    public void deleteById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public void addSection(Long lineId, SectionRequest request) {
        Line findLine = findLineById(lineId);

        Station upStation = findStationById(request.getUpStationId());
        if (!findLine.isOwnDownStation(upStation)) {
            throw new SectionException(ErrorCode.INVALID_UP_STATION_EXCEPTION);
        }

        Station downStation = findStationById(request.getDownStationId());
        if (findLine.isAlreadyOwnStation(downStation)) {
            throw new SectionException(ErrorCode.ALREADY_INCLUDED_STATION_EXCEPTION);
        }

        findLine.addSection(new Section(findLine, upStation, downStation, request.getDistance()));
    }

    public void deleteSection(Long lineId, Long stationId) {
        Line findLine = findLineById(lineId);
        Station findStation = findStationById(stationId);
        findLine.deleteLastSection(findStation);
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(NoSuchElementException::new);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NoSuchElementException::new);
    }

    private List<StationResponse> findAllStationInLine(Line line) {
        return line.getAllStation()
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}

