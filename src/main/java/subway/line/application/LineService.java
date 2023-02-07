package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.request.LineCreateRequest;
import subway.line.application.dto.request.LineUpdateRequest;
import subway.line.domain.Line;
import subway.line.domain.LineCommandRepository;
import subway.line.domain.LineQueryRepository;
import subway.line.exception.LineNotFoundException;
import subway.line.application.dto.request.SectionCreateRequest;
import subway.line.domain.Section;
import subway.station.application.StationService;
import subway.station.domain.Station;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationService stationService;
    private final LineQueryRepository lineQueryRepository;
    private final LineCommandRepository lineCommandRepository;

    public LineService(final LineQueryRepository lineQueryRepository,
                       final LineCommandRepository lineCommandRepository,
                       final StationService stationService) {
        this.lineQueryRepository = lineQueryRepository;
        this.lineCommandRepository = lineCommandRepository;
        this.stationService = stationService;
    }

    /**
     * 지하철 노선 목록 정보를 조회합니다.
     * 
     * @return 지하철 노선 목록 정보
     */
    public List<Line> findAllLines() {
        return lineQueryRepository.findAll();
    }

    /**
     * 지하철 노선 상세 정보를 조횧합니다.
     * 
     * @param lineId 지하철 노선 고유 번호
     * @return 지하철 노선 상세 정보
     */
    public Line findLineById(final Long lineId) {
        return lineQueryRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);
    }

    /**
     * 지하철 노선 정보를 등록합니다.
     *
     * @param lineCreateRequest 등록할 지하철 노선 정보
     * @return 등록된 지하철 노선 고유 번호
     */
    @Transactional
    public Long saveLine(final LineCreateRequest lineCreateRequest) {
        Line line = createLine(lineCreateRequest);
        lineCommandRepository.save(line);

        return line.getId();
    }

    /**
     * 지하철 노선 정보를 수정합니다.
     *
     * @param lineId 수정할 지하철 노선 고유 번호
     * @param lineUpdateRequest 수정할 지하철 노선 정보
     */
    @Transactional
    public void updateLine(final Long lineId, final LineUpdateRequest lineUpdateRequest) {
        Line line = findLineById(lineId);

        line.updateLine(lineUpdateRequest.toEntity());
    }

    /**
     * 지하철 노선 정보를 삭제합니다.
     * 
     * @param lineId 삭제할 지하철 노선 고유 번호
     */
    @Transactional
    public void deleteLine(final Long lineId) {
        lineCommandRepository.deleteById(lineId);
    }

    /**
     * 지하철 구간 정보를 등록합니다.
     * 
     * @param lineId 등록할 지하철 구간의 노선 고유 번호
     * @param sectionCreateRequest 등록할 지하철 구간 정보
     * @return 등록된 지하철 구간 고유 번호
     */
    @Transactional
    public Long saveSection(final Long lineId, final SectionCreateRequest sectionCreateRequest) {
        Line findLine = findLineById(lineId);
        Station upStation = stationService.findStationById(sectionCreateRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionCreateRequest.getDownStationId());
        Section section = Section.createSection(findLine, upStation, downStation, sectionCreateRequest.getDistance());

        findLine.getSections().addSection(section);

        return section.getId();
    }

    /**
     * 지하철 구간 정보를 삭제합니다.
     *
     * @param lineId 삭제할 지하철 구간의 노선 고유 번호
     * @param stationId 삭제할 지하철 구간 고유 번호
     */
    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line findLine = findLineById(lineId);
        Station findStation = stationService.findStationById(stationId);

        findLine.getSections().removeLastSection(findStation);
    }

    private Line createLine(final LineCreateRequest lineCreateRequest) {
        Station upStation = stationService.findStationById(lineCreateRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineCreateRequest.getDownStationId());

        return Line.createLine(lineCreateRequest.getName(), lineCreateRequest.getColor(),
                lineCreateRequest.getDistance(), upStation, downStation);
    }
}
