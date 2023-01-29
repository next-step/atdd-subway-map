package subway.presentation.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Station;
import subway.domain.SubwayLine;

public class SubwayLineRequest {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Create {

		private String name;

		private String color;

		private Long upStationId;

		private Long downStationId;

		private int distance;

		@Builder
		private Create(
			String name,
			String color,
			Long upStationId,
			Long downStationId,
			int distance) {
			this.name = name;
			this.color = color;
			this.upStationId = upStationId;
			this.downStationId = downStationId;
			this.distance = distance;
		}

		@JsonIgnore
		public List<Long> getUpAndDownStationIds() {
			return List.of(upStationId, downStationId);
		}

		public SubwayLine toEntity(List<Station> stations) {
			return new SubwayLine(
				this.name,
				this.color,
				this.upStationId,
				this.downStationId,
				stations
			);
		}
	}
}
