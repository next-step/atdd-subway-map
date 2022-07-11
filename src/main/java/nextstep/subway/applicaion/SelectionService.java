package nextstep.subway.applicaion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.SelectionRequest;
import nextstep.subway.applicaion.dto.SelectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Selection;
import nextstep.subway.domain.SelectionRepository;
import nextstep.subway.domain.Selections;

@Service
public class SelectionService {

	private LineRepository lineRepository;
	private SelectionRepository selectionRepository;

	public SelectionService(LineRepository lineRepository, SelectionRepository selectionRepository) {
		this.lineRepository = lineRepository;
		this.selectionRepository = selectionRepository;
	}

	@Transactional
	public SelectionResponse createSelection(long lineId, SelectionRequest selectionRequest) {
		Line line = lineRepository.findById(lineId)
			.orElseThrow(RuntimeException::new);
		line.isRegistrable(selectionRequest.getUpStationId(), selectionRequest.getDownStationId());
		line.updateDownStationId(selectionRequest.getDownStationId());
		return createSelectionResponse(selectionRepository.save(selectionRequest.toSelection(lineId)));
	}

	@Transactional
	public void deleteSelection(long lineId, long stationId) {
		Line line = lineRepository.findById(lineId)
			.orElseThrow(RuntimeException::new);
		line.isDeletable(stationId);

		Selections selections = new Selections(selectionRepository.findByLineIdOrderById(lineId));
		selections.isDeletable(stationId);

		Selection selection = selectionRepository.findByDownStationId(stationId)
			.orElseThrow(RuntimeException::new);
		selectionRepository.delete(selection);

	}

	public void getSelectionList(long lineId) {

	}

	public void getSelection(long lineId, long selectionId) {

	}

	private SelectionResponse createSelectionResponse(Selection selection) {
		return new SelectionResponse(selection.getId());
	}

}
