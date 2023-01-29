package subway.domain;

import java.util.List;

import subway.presentation.response.SubwayLineResponse;

public interface CustomSubwayLineRepository {

	List<SubwayLineResponse.LineInfo> findSubwayLineAll();
}
