package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.*;
import nextstep.subway.handler.error.custom.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.handler.error.custom.ErrorCode.*;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new BusinessException(FOUND_DUPLICATED_NAME);
        }
        Line savedLine = lineRepository.save(new Line(request.getName(), request.getColor()));

        return LineResponse.from(savedLine);
    }

    @Transactional(readOnly = true)
    public List<LineDetailResponse> getLineList() {
        return lineRepository.findAll().stream()
                .map(LineDetailResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineDetailResponse getLine(Long id) {
        return LineDetailResponse.from(findLineById(id));
    }

    public void patchLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);
        line.modify(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        if (!lineRepository.existsById(id)) {
            throw new BusinessException(LINE_NOT_FOUND_BY_ID);
        }
        lineRepository.deleteById(id);
    }

    public SectionResponse createSection(Long lineId, Long upStationId, Long downStationId, int distance) {
        return SectionResponse.from(sectionRepository.save(
                        Section.of(
                                findLineById(lineId),
                                findStationById(upStationId),
                                findStationById(downStationId),
                                distance)
                )
        );
    }

    @Transactional(readOnly = true)
    public List<SectionDetailResponse> getSections(Long id) {
        return findLineById(id).getSectionsResponse();
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new BusinessException(LINE_NOT_FOUND_BY_ID));
    }

    private Station findStationById(Long upStationId) {
        return stationRepository.findById(upStationId)
                .orElseThrow(() -> new BusinessException(STATION_NOT_FOUND_BY_ID));
    }
}
