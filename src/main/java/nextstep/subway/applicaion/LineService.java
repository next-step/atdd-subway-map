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
        Line savedLine = lineRepository.save(lineRequest.toEntity());
        saveEndpoint(lineRequest, savedLine);
        return LineResponse.convertedByEntity(savedLine);
    }


    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::convertedByEntity).collect(Collectors.toList());
    }

    public LineResponse getLine(long lineId) {
        Line line = lineRepository.findById(lineId)
            .orElseThrow(()->new RuntimeException(ExceptionMessages.getNoLineExceptionMessage(lineId)));
        return LineResponse.convertedByEntity(line);
    }

    @Transactional
    public LineResponse updateLine(LineRequest lineRequest, long lineId) {
        Line line = lineRequest.toEntity(lineId);
        Line updatedLine = lineRepository.save(line);
        return LineResponse.convertedByEntity(updatedLine);
    }

    @Transactional
    public void deleteLine(long lineId) {
        lineRepository.deleteById(lineId);
    }

    private Station registerStationOnLine(long stationId, Line savedLine) {
        Station upStation = stationRepository.findById(stationId)
            .orElseThrow(()->new RuntimeException(ExceptionMessages.getNoStationExceptionMessage(stationId)));
        upStation.addLine(savedLine);
        stationRepository.save(upStation);
        return upStation;
    }

    private void saveEndpoint(LineRequest lineRequest, Line savedLine) {
        Station upStation = registerStationOnLine(lineRequest.getUpStationId(), savedLine);
        savedLine.add(upStation);
        Station downStation = registerStationOnLine(lineRequest.getDownStationId(), savedLine);
        savedLine.add(downStation);
    }
}
