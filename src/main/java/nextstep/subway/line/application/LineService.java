package nextstep.subway.line.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.line.application.dto.CreateLineRequest;
import nextstep.subway.line.application.dto.CreateLineResponse;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.ModifyLineRequest;
import nextstep.subway.line.application.dto.ModifyLineResponse;

@Service
public class LineService {

	public CreateLineResponse createLine(CreateLineRequest request) {

		return null;
	}

	public List<LineResponse> getLines() {
		return null;
	}

	public LineResponse getLine(Long id) {
		return null;
	}

	public ModifyLineResponse modifyLine(Long id, ModifyLineRequest request) {
		return null;
	}

	public void deleteLine(Long id) {

	}
}
