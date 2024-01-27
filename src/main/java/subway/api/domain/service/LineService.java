package subway.api.domain.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import subway.api.interfaces.dto.LineCreateRequest;
import subway.api.interfaces.dto.LineResponse;
import subway.api.interfaces.dto.LineUpdateRequest;

/**
 * @author : Rene Choi
 * @since : 2024/01/27
 */
public interface LineService {
	LineResponse saveLine(LineCreateRequest createRequest);

	List<LineResponse> findAllLines();

	LineResponse findLineById(Long id);


	LineResponse updateLineById(Long id, LineUpdateRequest updateRequest);

	void deleteLineById(Long id);
}
