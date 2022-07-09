package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
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
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository
            .findById(lineRequest.getUpStationId())
            .orElseThrow(
                () -> new DataNotFoundException("Station 데이터가 없습니다.")
            );
        Station downStation = stationRepository
            .findById(lineRequest.getDownStationId())
            .orElseThrow(
                () -> new DataNotFoundException("Station 데이터가 없습니다.")
            );
        Line createLine = new Line(lineRequest, Arrays.asList(upStation, downStation));
        Line createdLine = lineRepository.save(createLine);
        return new LineResponse(createdLine);
    }

    public LineResponse findOneLine(Long id) {
        return new LineResponse(
            lineRepository.findById(id).orElseThrow(
                () -> new DataNotFoundException("Line 데이터가 없습니다.")
            )
        );
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(
            line -> new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStations().getStations())
        ).collect(Collectors.toList());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse editLine(Long id, LineRequest lineRequest) {
        Line findLine = lineRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException("Line 데이터가 없습니다.")
        );
        findLine.edit(lineRequest);
        return new LineResponse(findLine);
    }
}
