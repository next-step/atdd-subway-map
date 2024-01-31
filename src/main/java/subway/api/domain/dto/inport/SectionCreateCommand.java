package subway.api.domain.dto.inport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.api.interfaces.dto.SectionCreateRequest;
import subway.common.mapper.ModelMapperBasedVoMapper;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateCommand {

	private Long downStationId;
	private Long upStationId;
	private Long distance;

	public static SectionCreateCommand from(SectionCreateRequest createRequest){
		return ModelMapperBasedVoMapper.convert(createRequest, SectionCreateCommand.class);
	}
}
