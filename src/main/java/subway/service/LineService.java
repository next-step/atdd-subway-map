package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Station upStation = findStation(lineRequest.getUpStationId());
        Station downStation = findStation(lineRequest.getDownStationId());

        Line newLine = new Line(
                  lineRequest.getName()
                , lineRequest.getColor()
                , upStation
                , downStation
                , lineRequest.getDistance());

        Line saveLine = lineRepository.save(newLine);
        return LineResponse.of(saveLine);

    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(EntityNotFoundException::new);
    }
}
