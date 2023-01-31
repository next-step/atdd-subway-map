package subway.domain;

import java.util.List;
import java.util.Optional;

import subway.presentation.response.SubwayLineResponse;

public interface CustomSubwayLineRepository {

	List<SubwayLineResponse.LineInfo> findSubwayLineAll();

	Optional<SubwayLineResponse.LineInfo> findSubwayLineById(Long id);
}
