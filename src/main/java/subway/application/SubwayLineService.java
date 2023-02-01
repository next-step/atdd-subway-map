package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.domain.SubwayLine;
import subway.domain.SubwayLineRepository;
import subway.exception.NotFoundStationException;
import subway.exception.NotFoundSubwayLineException;
import subway.exception.SubwayLineErrorCode;
import subway.presentation.request.SubwayLineRequest;
import subway.presentation.response.SubwayLineResponse;

@Service
@RequiredArgsConstructor
public class SubwayLineService {

	private final SubwayLineRepository subwayLineRepository;

	private final StationRepository stationRepository;

	private final SectionRepository sectionRepository;

	@Transactional
	public SubwayLineResponse.CreateInfo createSubwayLine(SubwayLineRequest.Create createRequest) {
		List<Station> stations = stationRepository.findByIdIn(createRequest.getUpAndDownStationIds());

		if (stations.isEmpty()) {
			throw new NotFoundStationException(SubwayLineErrorCode.NOT_FOUND_STATION);
		}

		SubwayLine subwayLine = createRequest.toEntity(stations);

		sectionRepository.save(subwayLine.createSection(createRequest.getDistance()));

		return new SubwayLineResponse.CreateInfo(
			subwayLineRepository.save(
				subwayLine
			)
		);
	}

	@Transactional(readOnly = true)
	public List<SubwayLineResponse.LineInfo> findSubwayLines() {
		return subwayLineRepository.findSubwayLineAll();
	}

	@Transactional(readOnly = true)
	public SubwayLineResponse.LineInfo findSubwayLineById(Long id) {
		return subwayLineRepository.findSubwayLineById(id)
			.orElseThrow(() -> new NotFoundSubwayLineException(SubwayLineErrorCode.NOT_FOUND_SUBWAY_LINE));
	}

	@Transactional
	public SubwayLineResponse.UpdateInfo update(Long id, SubwayLineRequest.Update updateRequest) {
		SubwayLine subwayLine = subwayLineRepository.findById(id)
			.orElseThrow(() -> new NotFoundSubwayLineException(SubwayLineErrorCode.NOT_FOUND_SUBWAY_LINE));

		subwayLine.updateInfo(
			updateRequest.getName(),
			updateRequest.getColor()
		);

		return new SubwayLineResponse.UpdateInfo(subwayLine);
	}

	@Transactional
	public void deleteById(Long id) {
		SubwayLine subwayLine = subwayLineRepository.findById(id)
			.orElseThrow(() -> new NotFoundSubwayLineException(SubwayLineErrorCode.NOT_FOUND_SUBWAY_LINE));

		subwayLineRepository.delete(subwayLine);
	}
}
