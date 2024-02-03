# Wee1 Step 2 이슈


## 도메인 모델링

요구사항을 처음 받고 생각을 했을 때 연관관계 매핑이 잘 떠오르지 않았습니다. 회사에서는 JPA 애노테이션 방식의 매핑을 지양하고, pk 필드 매핑 방식을 사용하고 있고 그에 따라 조합하거나 생성하는 로직을 JPA에 위임하지 않고 직접 호출하기 때문에 그 방식에 조금 익숙해져 있었던 것 같아요.

처음 접근한 방식은 Line과 Station이 다대다로 매핑하는 것이었는데, 이 방식으로는 잘 풀리지가 않더라구요. 실제 세계의 지하철 컨셉을 다시 고려해서 생각해 보니 지하철 역, 그리고 역과 역 사이의 연결, 그리고 그것을 포괄하는 노선이라는 개념으로 접근할 수 있었습니다. 고민했던 부분은 Aggregate로 만들 수 있는 포인트가 있을지였는데, 이 부분에 있어서도 뚜렷한 답을 찾지는 못해 3개의 구별된 엔티티로 만들게 되었습니다.


## 도메인 경계와 패키지 구조

패키지를 짜면서 고민했던 부분은 Service 이하의 트랜잭션 컨텍스트가 필요한 상황에서, 도메인을 Station과 Line으로 분리하자니, 각각의 Service에서 다른 Repository를 호출하게 되는 점이었습니다. 즉, 패키지를 Station, Line으로 분리하면, StationService->StationRepository, LineService->LineRepository가 나올 텐데, StationService에서 LineRepository를 참조한다거나 혹은 그 반대 상황이 발생하는 것이죠. 코드 작동상에서는 문제될 것은 없겠지만 컨셉상으로 조금 와닿지가 않더라구요. 아키텍처 쟁점은 본 과정의 주 포인트는 아니었지만, 평소에 익숙하게 사용하는 방식이 아니라 그런지 조금 어색했습니다.

결론적으로는 Station과 Line과 Link라는 엔티티 별 패키지를 해체하고, 비즈니스 도메인 컨텍스트라는 개념이라는 점에서 통합된 패키지로 구성하고, 3티어 아키텍처 기반에서 domain과 infra계층의 의존성이 역전된 operators를 두어 조금 더 유연한 구조로 풀어보고자 했습니다.


## Cascade 옵션에 대해

처음 작성한 엔티티 모델링에서는 Line과 Link가 1대다 관계를 맺을 때 `CascadeType.ALL` 옵션으로 설정을 했었는데요. 이 부분을 리팩토링했습니다.

두 가지 관점에서 보았는데,

- Line과 Link 사이의 관점에서는 지하철 노선이 없어진다면, 그 노선에 속한 구건(Link)도 더 이상 존재할 이유가 없으므로 이 경우 CascadeType.ALL은 적절한 옵션이 맞을 것입니다.
-  Line과 Station 사이의 관점에서는 노선이 삭제된다고 하더라도 연관된 지하철 역이 사라져서는 안되기 때문에 Link 와 Station 사이에서는 Cascade 옵션을 적용하면 안 됩니다.

결론적으로 해당 필드에 `CascadeType.ALL` 옵션 적용은 불필요한 것으로 판단할 수 있었습니다.

그런데 이것을 생각할 수 있었던 포인트가 Test 코드에서 @Sql을 적용하려는 h2에서 Truncate Table에서 Cascade 제약 때문에 실패하는 예외 때문입니다.

## DirtyContext -> @Sql

처음 방식에서는 테스트 코드의 DB 격리를 위해 `@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)` 애노테이션을 사용했습니다.

힌트에서 주어진 내용에 따라 리팩토링하는 방식으로 @Sql 사용하는 방식으로 변경하였는데요.

`@DirtiesContext` 방식은 테스트가 많아질수록 속도 저하가 심해져 사용하기가 힘들다는 점에 공감하고 있었는데 좋은 대안이 있어서 적용해보고 싶었습니다.

그런데 `@Sql`이 잘 적용이 안되더라구요. 결국에는 아래와 같은 방식으로 작성해서 동작하는 것을 확인했습니다.

SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE link RESTART IDENTITY;
TRUNCATE TABLE line RESTART IDENTITY;
TRUNCATE TABLE station RESTART IDENTITY;
SET REFERENTIAL_INTEGRITY TRUE;


<img width="622" alt="image" src="https://github.com/next-step/atdd-subway-map/assets/115696395/82d6cbdc-6e7c-4f45-80a5-a284dba142d0">

refrence: https://www.h2database.com/html/commands.html#truncate_table


한가지 todo 사항으로 남겨둔 부분은 현재는 초기화 sql이 하드코딩으로 되어 있는데, 동적으로 테이블을 인식해서 테이블 클린징 로직을 자동화할 수 있으면 좋을 것 같습니다.



## HttpRequest util에 대한 추상화

이번 스텝에서 Line 부분을 추가로 구현하게 되면서 Step 1에서 Station과의 중복 코드가 많아지게 되었어요. 요구사항에서 힌트를 주신 것처럼 이부분을 해결하는 방법을 고민해보았습니다.

가급적 interface를 통해 좀 더 유연한 구조를 구현하고 싶었지만, 테스트 코드에서 static 호출하는 것의 편의성을 놓치고 싶지 않았습니다. 따라서 interface를 통해 Overiding하는 경우 static 한 호출이 안되기 때문에 abstract class로 풀이 했습니다. 또한 execute 메서드 하나만 두고 내부에서 분기를 처리하는 것 보다 HttpMethod 별로 메서드를 만드는 정도의 구체화가 필요하다고 보았습니다. 





---

원문 글 링크 
- https://github.com/next-step/atdd-subway-map/pull/1045#discussion_r1469659660