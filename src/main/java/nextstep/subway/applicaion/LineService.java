package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.*;
import nextstep.subway.domain.exception.LineNotFoundException;
import nextstep.subway.domain.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Long upStationId = lineRequest.getUpStationId();
        Long downStationId = lineRequest.getDownStationId();

        Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(() -> new StationNotFoundException(upStationId));

        Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(() -> new StationNotFoundException(downStationId));

        Line line = lineRepository.save(lineRequest.toEntity(upStation, downStation));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long lineId) {
        return createLineResponse(findById(lineId));
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }

    @Transactional
    public void updateLine(Long lineId, LineUpdateRequest updateRequest) {
        Line line = findById(lineId);

        if (StringUtils.hasText(updateRequest.getName())) {
            line.updateName(updateRequest.getName());
        }

        if (StringUtils.hasText(updateRequest.getColor())) {
            line.updateColor(updateRequest.getColor());
        }
    }

    private Line findById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    @Transactional
    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
