package subway.presentation.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Section;
import subway.domain.Station;

public class SectionRequest {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {

		private Long upStationId;

		private Long downStationId;

		private int distance;

		@Builder
		private Create(Long downStationId, Long upStationId, int distance) {
			this.downStationId = downStationId;
			this.upStationId = upStationId;
			this.distance = distance;
		}

		@JsonIgnore
		public List<Long> getUpAndDownStationIds() {
			return List.of(upStationId, downStationId);
		}

		public Section toEntity(List<Station> stations) {
			return new Section(upStationId, downStationId, distance, stations);
		}
	}
}
