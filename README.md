<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>
<br>

## 🚀 화이팅

|주차|실습|날짜|
|:---:|:---:|:---:|
|1주차|노선 관리 기능|22/01/22|
|1주차|인수 테스트 리팩터링|22/01/25|
|1주차|지하철 구간 관리 기능|22/01/27|

# 1주차
## 시나리오
- [x] 지하철 노선 생성
- [x] 지하철 노선 목록 조회
- [x] 지하철 노선 조회
- [x] 지하철 노선 수정
- [x] 지하철 노선 삭제

- [x] 지하철 구간 등록
- [x] 지하철 구간 삭제

## 지하철 구간 관리 요구사항
- [x] 새로운 구간의 상행선은 기존 구간의 하행선이여야한다.   SectionValidator#validateUpStation()
- [x] 새로운 구간의 하행선은 해당 노선에 속하지 않아야 한다. SectionValidator#validateDownStation()
- [x] 거리가 0이거나 0보다 작으면 안된다. SectionValidator#validateDistance()

## 리뷰 사항
- [x] 메서드 호출 위치 변경
- [x] 테스트를 위해 필요한 Step 클래스로의 분리
- [x] 재할당 필요없는 변수에 대해 final 추가 (response)
- [x] 컨벤션에 맞는 코드 작성 순서
- [x] null 반환 삭제 및 Custom Exception handling 로직 추가
- [x] 인수 테스트 검증 불필요한 분리제거
- [x] 정정팩토리메서드 사용시 생성자 private
- [x] 정적팩터리메서드 생성자 밑으로 이동
- [x] 노선 생성 시 두 종점역 추가하기
- [x] 테스트 목적에 맞게 설명 상세히 Display 하기 (설명하듯이)
- [x] 구간이 1개인 경우 역 삭제가 불가능한 인수테스트 진행
- [x] Section 의 cascade 설정
- [x] stream() 재적용


## 강의 피드백 사항
- [x] 지하철 역 이름 중복 생성 금지
- [x] 지하철 노선 이름 중복 생성 금지