package subway.api.domain.service;

import java.util.List;

import subway.api.domain.dto.inport.LineCreateCommand;
import subway.api.domain.dto.inport.LineUpdateCommand;
import subway.api.interfaces.dto.request.LineCreateRequest;
import subway.api.interfaces.dto.response.LineResponse;
import subway.api.interfaces.dto.request.LineUpdateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LineService {
	LineResponse saveLine(LineCreateCommand createRequest);

	List<LineResponse> findAllLines();

	LineResponse findLineById(Long id);


	LineResponse updateLineById(Long id, LineUpdateCommand updateRequest);

	void deleteLineById(Long id);
}
