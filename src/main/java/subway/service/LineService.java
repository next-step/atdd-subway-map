package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.controller.dto.LineCreateRequest;
import subway.domain.LineRepository;
import subway.controller.dto.LineResponse;
import subway.controller.dto.LineUpdateRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/** 지하철 노선 관리 담당 서비스 */
@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    /**
     * 주어진 지하철 노선 생성 데이터로 지하철 노선을 생성 후 생성 정보를 반환합니다.
     *
     * @param createRequest 지하철 노선 생성 데이터
     * @return 생성된 지하철 노선 정보
     */
    public LineResponse createLine(LineCreateRequest createRequest) {
        // TODO: 주어진 상행종점역, 하행종점역이 존재하는지 검증 추가
        Line line = lineRepository.save(
            Line.of(
                createRequest.getName(),
                createRequest.getColor(),
                createRequest.getUpStationId(),
                createRequest.getDownStationId(),
                createRequest.getDistance()
            )
        );

        // TODO: 실제 역 응답값에 추가
        return LineResponse.of(line, List.of());
    }

    /**
     * 모든 지하철 노선 정보를 반환합니다.
     *
     * @return 모든 지하철 노선 정보
     */
    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
            .stream().map(line -> LineResponse.of(line, List.of()))
            .collect(Collectors.toList());
    }

    /**
     * 주어진 지하철 노선 식별자로 찾은 노션의 정보를 반환합니다. 찾지못하면 예외를 던집니다.
     *
     * @param lineId 지하철 노선 식별자
     * @return 지하철 노선 정보
     * @throws EntityNotFoundException 식별자에 해당하는 지하철노선을 찾지 못한 경우 던짐
     */
    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        Line line = findLineById(lineId);

        // TODO: 실제 역 응답값에 추가
        return LineResponse.of(line, List.of());
    }

    /**
     * 주어진 지하철 노선 식별자로 찾은 노선의 정보를 주어진 변경 정보로 수정합니다.
     * 지하철 노선을 찾지 못하면 예외를 던집니다.
     *
     * @param lineId 지하철 노선 식별자
     * @param updateRequestDto 지하철 노선 변경 데이터
     * @throws EntityNotFoundException 식별자에 해당하는 지하철노선을 찾지 못한 경우 던짐
     */
    public void updateLine(Long lineId, LineUpdateRequest updateRequestDto) {
        Line line = findLineById(lineId);

        lineRepository.save(
            line.update(updateRequestDto.getName(), updateRequestDto.getColor())
        );
    }

    /**
     * 주어진 지하철 노선 식별자로 찾은 노선을 삭제합니다. 만약 찾지 못하면 예외를 던집니다.
     *
     * @param lineId 지하철 노선 식별자
     * @throws EntityNotFoundException 식별자에 해당하는 지하철노선을 찾지 못한 경우 던짐
     */
    public void deleteLine(Long lineId) {
        lineRepository.delete(findLineById(lineId));
    }

    /** 주어진 지하철 노선 식별자로 찾은 노선정보 엔티티 반환. 찾지못하면 예외 던짐 */
    private Line findLineById(final Long lineId) {
        // TODO: 익셉션 정의?
        return lineRepository.findById(lineId)
            .orElseThrow(EntityNotFoundException::new);
    }
}
