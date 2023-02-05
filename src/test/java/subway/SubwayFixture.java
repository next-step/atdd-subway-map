package subway;

public class SubwayFixture {

    public static final Station SUNGSOO_STATION = new Station(1L, "성수역");
    public static final Station GANG_NAM_STATION = new Station("강남역");
    public static final Station GWANG_KYO_STATION = new Station("광교역");
    public static final LineRequest LINE_SHIN_BUN_DANG_REQUEST = new LineRequest(
        "신분당선", "bg-red-600", 2L, 3L, 10);
    public static final LineRequest LINE_TWO_REQUEST = new LineRequest(
        "2호선", "bg-green-600", 2L, 3L, 5);
    public static final LineRequest LINE_NEW_SHIN_BUN_DANG_REQUEST = new LineRequest(
        "새로운 신분당선", "bg-red-500", 2L, 3L, 10);
}
