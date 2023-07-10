package subway.common.exception;

// TODO: 없는 db 리소스에 대한 질의의 경우 어떤 status code를 반환하나요?
// 200 -> body에서 응답 결과를 싣는다 (봉투 패턴)
// 204 -> 404는 클라이언트에서 잘못된 "약속"에 사용한다는 관점 (정의되지 않은 리소스 접근), 그러나 body가 없으므로 에러 메시지 전달 못함. 연관 관계에 대한 질의 요청시에는 충분한 원인 전달 못할 수 있음
// 404 -> 204는 Update / Delete에 사용하고, 리소스가 존재하지 않는다는 관점
// 200 -> 204 -> 400 순으로 적당하다고 생각하는데 다른 분들은 어떻게 사용하시는지 궁금합니다
public class ResourceNotFoundException extends IllegalStateException {

    public ResourceNotFoundException(Class entityClass, Long id) {
        super(String.format("존재하지 않는 리소스입니다. 요청한 리소스 : %s, id : %d", entityClass.getName(), id));
    }
}
