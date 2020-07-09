package nextstep.subway.station.acceptance.step;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.LineStationResponse;
import nextstep.subway.station.dto.StationResponse;

public class LineStationRemoveAcceptanceStep {

	public static ExtractableResponse<Response> 노선에_지하철역_제외(Long lineId, Long lineStationId) {
		return RestAssured.given().log().all()
			.when()
			.delete("/lines/{lineId}/stations/{stationId}", lineId, lineStationId)
			.then()
			.log().all()
			.extract();
	}

	public static boolean 지하철_노선에_지하철역_순서_정렬됨(ExtractableResponse<Response> lineResponse,
		List<Long> stationIdsInOrder) {
		List<Long> parsedStationIds = parseIdsInPreStationIdOrders(lineResponse);
		return IntStream.range(0, parsedStationIds.size())
			.allMatch(index -> parsedStationIds.get(index).equals(stationIdsInOrder.get(index)));
	}

	public static List<Long> parseIdsInPreStationIdOrders(ExtractableResponse<Response> lineResponse) {
		return lineResponse.as(LineResponse.class).getStations().stream()
			.sorted(Comparator.comparing(LineStationResponse::getPreStationId,
				Comparator.nullsFirst(Comparator.naturalOrder())))
			.map(LineStationResponse::getStation)
			.map(StationResponse::getId)
			.collect(Collectors.toList());
	}

	public static boolean 지하철_노선에_삭제된_역이_이전번호로_존재하지_않음(ExtractableResponse<Response> lineResponse,
		Long deletedLineStationId) {
		return lineResponse.as(LineResponse.class).getStations().stream()
			.noneMatch(station -> {
				Long preStationId = station.getPreStationId();
				if (preStationId == null) {
					return true;
				}
				return preStationId.equals(deletedLineStationId);
			});
	}
}
