package nextstep.subway.applicaion.dto;

public class SelectionResponse {
	private long selectionId;

	public SelectionResponse(long selectionId) {
		this.selectionId = selectionId;
	}

	public long getSelectionId() {
		return selectionId;
	}
}
