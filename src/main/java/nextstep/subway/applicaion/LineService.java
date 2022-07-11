package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.ExceptionMessages;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Line line = lineRepository.save(lineRequest.toEntity());
        saveEndpoints(lineRequest, line);
        Line savedLine = lineRepository.findById(line.getId()).orElseThrow();
        return LineResponse.convertedByEntity(savedLine);
    }


    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::convertedByEntity).collect(Collectors.toList());
    }

    public LineResponse getLine(long lineId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoLineExceptionMessage(lineId)));
        return LineResponse.convertedByEntity(line);
    }

    @Transactional
    public LineResponse updateLine(LineRequest lineRequest, long lineId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoLineExceptionMessage(lineId)));
        line.changeName(lineRequest.getName());
        line.changeColor(lineRequest.getColor());
        Line updatedLine = lineRepository.save(line);
        return LineResponse.convertedByEntity(updatedLine);
    }

    @Transactional
    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }


    private void saveEndpoints(LineRequest lineRequest, Line savedLine) {
        long upStationId = lineRequest.getUpStationId();
        long downStationId = lineRequest.getDownStationId();

        Station upStation = stationRepository.findById(upStationId)
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoStationExceptionMessage(upStationId)));
        savedLine.addUpEndpointStation(upStation);

        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
            .orElseThrow(() -> new RuntimeException(ExceptionMessages.getNoStationExceptionMessage(downStationId)));
        savedLine.addDownEndpointStation(downStation);

        lineRepository.save(savedLine);
    }
}
