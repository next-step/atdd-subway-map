package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineIncludingStationsResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private SectionRepository sectionRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = findUpStationById(request.getUpStationId());
        Station downStation = findDownStationById(request.getDownStationId());

        Line line = new Line(request.getName(), request.getColor());
        if (Objects.nonNull(upStation) && Objects.nonNull(downStation)) {
            line.addSection(upStation, downStation, request.getDistance());
        }

        return new LineResponse(lineRepository.save(line));
    }

    private Station findUpStationById(Long upStationId) {
        Station upStation = null;
        if (Objects.nonNull(upStationId)) {
            upStation = stationRepository.findById(upStationId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "노선의 구간 초기화 중 상행선역을 찾을 수 없습니다. upStationId:" + upStationId));
        }
        return upStation;
    }

    private Station findDownStationById(Long downStationId) {
        Station downStation = null;
        if (Objects.nonNull(downStationId)) {
            downStation = stationRepository.findById(downStationId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "노선의 구간 초기화 중 하행선역을 찾을 수 없습니다. downStationId:" + downStationId));
        }
        return downStation;
    }

    @Transactional(readOnly = true)
    public List<LineIncludingStationsResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineIncludingStationsResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineIncludingStationsResponse findById(Long id) {
        return new LineIncludingStationsResponse(lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철 역을 찾을 수 없습니다. id = " + id)));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("지하철 역을 찾을 수 없습니다. id = " + id));
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
