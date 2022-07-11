package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());

        Line createLine = new Line(
            null, lineRequest.getName(), lineRequest.getColor(),
            lineRequest.getDistance(), Arrays.asList(upStation, downStation)
        );

        Line createdLine = lineRepository.save(createLine);
        return getLineResponse(createdLine);
    }

    @Transactional(readOnly = true)
    public LineResponse findOneLine(Long id) {
        Line findLine = getLine(id);
        return getLineResponse(findLine);
    }
    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(this::getLineResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse editLine(Long id, LineRequest lineRequest) {
        Line findLine = getLine(id);
        findLine.edit(lineRequest);
        return getLineResponse(findLine);
    }

    private LineResponse getLineResponse(Line createdLine) {
        return new LineResponse(
            createdLine.getId(), createdLine.getName(), createdLine.getColor(),
            createdLine.getStations().getStations().stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList()));
    }

    private Station getStation(Long stationId) {
        return stationRepository
            .findById(stationId)
            .orElseThrow(
                () -> new DataNotFoundException("Station 데이터가 없습니다.")
            );
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException("Line 데이터가 없습니다.")
        );
    }


}
