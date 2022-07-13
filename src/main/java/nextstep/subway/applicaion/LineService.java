package nextstep.subway.applicaion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.BusinessException;
import nextstep.subway.exception.ErrorCode;

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
	public LineResponse createLine(LineRequest lineRequest) {
		Station upStation = getStation(lineRequest.getUpStationId());
		Station downStation = getStation(lineRequest.getDownStationId());
		Line line = lineRequest.toEntity(new Section(upStation, downStation, lineRequest.getDistance()));
		return LineResponse.from(lineRepository.save(line));
	}

	private Station getStation(Long stationId) {
		return stationRepository.findById(stationId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

	public List<LineResponse> getAllLine() {
		return LineResponse.fromList(lineRepository.findAll());
	}

	public LineResponse getLine(Long lineId) {
		return LineResponse.from(getLineById(lineId));
	}

	private Line getLineById(Long lineId) {
		return lineRepository.findById(lineId).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
	}

	@Transactional
	public void deleteLine(Long lineId) {
		lineRepository.delete(getLineById(lineId));
	}

	@Transactional
	public void updateLine(Long lineId, LineUpdateRequest lineUpdateRequest) {
		getLineById(lineId).update(lineUpdateRequest);
	}

	@Transactional
	public LineResponse addSection(Long lineId, SectionRequest sectionRequest) {
		Line line = getLineById(lineId);
		Station upStation = getStation(sectionRequest.getUpStationId());
		Station downStation = getStation(sectionRequest.getDownStationId());
		line.addSection(new Section(upStation, downStation, sectionRequest.getDistance()));
		return LineResponse.from(line);
	}
}
