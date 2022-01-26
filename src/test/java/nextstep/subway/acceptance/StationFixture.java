package nextstep.subway.acceptance;

import nextstep.subway.applicaion.dto.StationRequest;

public class StationFixture {

    public static final String 시청역 = "시청역";
    public static final String 서울역 = "서울역";
    public static final String 용산역 = "용산역";
    public static final String 강남역 = "강남역";
    public static final String 역삼역 = "역삼역";
    public static final String 선릉역 = "선릉역";
    public static final StationRequest FIXTURE_시청역 = StationRequest.from(시청역);
    public static final StationRequest FIXTURE_서울역 = StationRequest.from(서울역);
    public static final StationRequest FIXTURE_용산역 = StationRequest.from(용산역);
    public static final StationRequest FIXTURE_강남역 = StationRequest.from(강남역);
    public static final StationRequest FIXTURE_역삼역 = StationRequest.from(역삼역);
    public static final StationRequest FIXTURE_선릉역 = StationRequest.from(선릉역);

}
