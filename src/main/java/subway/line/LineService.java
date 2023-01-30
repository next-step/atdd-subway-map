package subway.line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import subway.Station;
import subway.StationRepository;
import subway.StationResponse;

@Service
public class LineService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Station upStation = stationRepository.findById(lineRequest.getUpStationsId()).orElse(null);
		Station downStation = stationRepository.findById(lineRequest.getDownStationsId()).orElse(null);

		Line saveLine = lineRepository.save(new Line(
			lineRequest.getName(), lineRequest.getColor(),
			upStation, downStation, lineRequest.getDistance()));

		return createLineResponse(saveLine);
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
			.map(this::createLineResponse)
			.collect(Collectors.toList());
	}

	public LineResponse findLineById(Long id) {
		return createLineResponse(lineRepository.findById(id).orElse(null));
	}

	@Transactional
	public void updateLineById(Long id, LineUpdateRequest lineUpdateRequest) {
		Line line = lineRepository.findById(id).orElse(null);
		line.setName(lineUpdateRequest.getName());
		line.setColor(lineUpdateRequest.getColor());
	}

	@Transactional
	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	private LineResponse createLineResponse(Line line) {
		try {
			StationResponse upStationResponse =
				new StationResponse(line.getUpStationId().getId(), line.getUpStationId().getName());
			StationResponse downStationResponse =
				new StationResponse(line.getDownStationId().getId(), line.getDownStationId().getName());

			List<StationResponse> stations = new ArrayList<>();
			stations.add(upStationResponse);
			stations.add(downStationResponse);

			return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
		} catch (NullPointerException npe) {
			return new LineResponse(-1L, "", "", null);
		}
	}
}
