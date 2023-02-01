package subway.presentation.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.application.Section;

public class SectionRequest {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {

		private Long downStationId;

		private Long upStationId;

		private int distance;

		@Builder
		private Create(Long downStationId, Long upStationId, int distance) {
			this.downStationId = downStationId;
			this.upStationId = upStationId;
			this.distance = distance;
		}

		public Section toEntity() {
			return new Section(upStationId, downStationId, distance);
		}
	}
}
