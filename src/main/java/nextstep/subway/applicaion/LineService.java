package nextstep.subway.applicaion;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineEndpoint;
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
        Line createdLine = lineRepository.save(lineRequest.toEntity());
        List<StationResponse> lineEndpoints = getLineEndPoints(lineRequest);
        return new LineResponse(createdLine.getId(),createdLine.getName(),createdLine.getColor(),lineEndpoints);
    }

    private List<StationResponse> getLineEndPoints(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
            .orElseThrow(()-> new RuntimeException("해당 역이 없습니다."));
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
            .orElseThrow(()-> new RuntimeException("해당 역이 없습니다."));
        return new LineEndpoint(upStation, downStation).toStationResponse();
    }
}
