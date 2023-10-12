# TestCode

💡 요구사항
- 주문 목록에 음료 추가/삭제 기능
- 주문 목록 전체 지우기
- 주문 목록 총 금액 계산하기
- 주문 생성하기
- 가게 운영 시간(10:00~22:00) 외에는 주문을 생성할 수 없다.

### Chapter1 단위 테스트
✏ 키워드: 단위테스트, 수동 테스트, 자동화 테스트, Junit5, AssertJ, 해피케이스, 예외케이스, 경계값 테스트, 테스트 하기 쉬운/어려운(순수함수)

📕 추가학습: @Data, @Setter, @AllArgsConstructor 지양, 양방향 연관관계 시 @ToString 순환 참조 문제

### Chapter2 TDD
✏ 키워드: TDD, 레드-그린-리팩토링

📕 추가학습: 애자일 방법론, 익스트림 프로그래밍, 스크럼, 칸반

### Chapter3 테스트는 []다.
✏ 키워드: @DisplayName, Given/When/Then - 주어진 환경, 행동, 상태 변화, TDD vs BDD

📕 추가학습: Junut vs Spock

💡 요구사항 추가
- 키오스크 주문을 위한 상품 후보 리스트 조회
- 상품의 판매 상태: 판매중, 판매보류, 판매중지
  -> 판매중, 판매보류인 상태의 상품을 화면에 보여줌
- id, 상품 번호, 상품 타입, 판매 상태, 상품 이름, 가격
- 상품 번호 리스트를 받아 주문 생성하기
- 주문은 주문 상태, 주문 등록 시간을 가진다
- 주문의 총 금액을 계산할 수 있어야 한다

### Chapter4 Spring & JPA 기반 테스트
✏ 키워드: Layered Architecture, 단위 테스트 vs 통합테스트, IoC, DI, AOP, ORM, 패러다임의 불일치, Hibernate, Spring Data JPA
           @RestControllerAdvice, @ExceptionHandler, Spring bean validation, @WebMvcTest, ObjectMapper, Mock, Mockito, @MockBean
           
📕 추가학습: Hexagonal Architecture

💡 요구사항 추가
- 주문 생성 시 재고 확인 및 개수 차감 후 생성하기
- 재고는 상품번호를 가진다
- 재고와 관련 있는 상품 타입은 병 음료, 베이커리이다
- 관리자 페이지에서 신규 상품을 등록할 수 있다
- 상품명, 상품 타입, 판매 상태, 가격 등을 입력받는다

### Chapter5 Mock을 마주하는 자세
✏ 키워드: Test Double Stubbing, @Mock, @MockBean, @Spy, @InjectMocks, BDDMockito, Classicist vs Mockist

📕 추가학습:  @SpyBean 

#### Test Double
✔ Dummy: 아무것도 하지 않는 깡통 객체 

✔ Fake: 단순한 형태로 동일한 기능을 수행하나, 프로덕션에서 쓰기에는 부족한 객체(ex.FakeRepository) 

❗ Stub: 테스트에서 요청한 것에 대해 미리 준비한 결과를 제공하는 객체, 그 외에는 응답하지 않는다 

✔ Spy: Stub이면서 호출된 내용을 기록하여 보여줄 수 있는 객체, 일부는 실제 객체처럼 동작시키고 일부만 Stubbing 할 수 있다 

❗ Mock: 행위에 대한 기대를 명세하고, 그에 따라 동작하도록 만들어진 객체 

Stub 과 Mock의 차이
Stub: 상태검증, Mock: 행위검증

