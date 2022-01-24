package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LineService {
	private LineRepository lineRepository;

	public LineService(LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public LineResponse saveLine(LineRequest request) {
		Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
		return LineResponse.of(line);
	}

	public List<LineResponse> findAllLines() {
		List<Line> lines = lineRepository.findAll();
		return lines.stream()
			.map(LineResponse::of)
			.collect(Collectors.toList());
	}

	public LineResponse findById(Long id) {
		Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
		return LineResponse.of(line);
	}

	public void updateLine(Long id, LineRequest lineRequest) {
		Line line = lineRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
		line.update(lineRequest.getName(), lineRequest.getColor());
	}

	public void deleteLine(Long id) {
		lineRepository.deleteById(id);
	}
}
