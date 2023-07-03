# Chat service
스프링부트 웹소켓 Stomp를 이용한 채팅서비스

## 주요 참조 문서

stomp 프로토콜
> https://stomp.github.io/stomp-specification-1.2.html

스프링 stomp
> https://docs.spring.io/spring-framework/reference/web/websocket/stomp.html

> https://docs.spring.io/spring-framework/docs/4.3.x/spring-framework-reference/html/websocket.html

rabbitmq stomp
> https://www.rabbitmq.com/stomp.html

## 구성
### 프론트
* HTML
* JavaScript
* css

### 백엔드
* SpringBoot
* mysql
* JPA
* RabbitMq

## 인프라
![image](https://github.com/hongseungjae/chat-service/assets/41093183/35dc5856-5622-4f86-bb94-73b8e273f0f1)

## 주요 이슈
#### 
* 스프링부트 websocket을 분산환경에서 사용하기위해 메시지큐를 도입
  - 스프링 컨테이너 메모리에 존재하는 브로커채널을 분산환경에서는 사용불가
  - RabbitMQ를 도입하여 해당 문제를 해결
  - https://hong-good.tistory.com/9
 
* 스프링부트 웹소켓 stomp 환경에서 이벤트 활용하기
  - 채팅방에 입장 시 입장 메시지와 퇴장 시 퇴장 메시지를 뿌리기 위해 스프링 이벤트를 활용하여 처리
  - https://hong-good.tistory.com/7
 

* Spring Security 403 response 해결
  - 알 수 없는 이유로 권한 에러가 발생
  - 로그 레벨을 높이고, 디버깅을 통한 문제 해결
  - https://hong-good.tistory.com/5

* 스프링부트 security에서 인증이 실패하는 경우 발생하는 과정
  - 인증 실패 시 401 응답이 발생
  - 디버깅을 하여 커스텀 포인트를 발견하고 해당 에러를 잡기위한 커스텀 클래스를 작성하여 처리
  - https://hong-good.tistory.com/6

## 경험
<details>
    <summary>N:M 관계에서 1:N, M:1 관계로 풀기</summary>

* 채팅방(Room)과 유저(Member)의 관계는 다대다 입니다. 하나의 유저가 여러 채팅 방에 속해있을 수 있으며, 한 개의 방에는 여러명의 유저가 있게 됩니다.
JPA에서는 다대다 관계를 @ManyToMany를 이용해서 매핑 시킵니다. @ManyToMany를 사용하면 둘 테이블을 매핑시키기 위한 중간 테이블이 생깁니다. 
이 중간 테이블에는 다음과 같은 단점이 있습니다.
  1. 매핑정보만 들어가고 추가 데이터를 넣는 것이 불가능하다. 
  2. 중간 테이블이 숨겨져 있기 때문에 쿼리가 예상하지 못하는 형태로 나간다.
* 따라서 이런 문제를 해결하기 위해서는 중간에 엔티티를 따로 두는 방식으로 풀어낼 수 있습니다.

Member - Participant - Room 과 같은 관계로 풀어낼 수 있습니다.

* https://www.inflearn.com/questions/435090/%EB%8B%A4%EB%8C%80%EB%8B%A4-%EA%B4%80%EA%B3%84%EA%B0%80-%EC%95%88%EC%A2%8B%EC%9D%80-%EC%9D%B4%EC%9C%A0

</details>

<details>
    <summary> JPA의 Id값은 int나 long 기본자료형이 아닌 Long, Integer 참조형을 사용하는 이유</summary>

1. null을 가질 수 있습니다. 이 의미는 0으로 값을 의도한 것인지, 아니면 단순 값을 초기화 안한건지 파악할 수 있습니다. 

2. Validate에서 @NotNull 사용할 수 있습니다.

   - 그 외에 필드 값의 경우 null을 관리해야하는지에 따라 선택적으로 사용하면 된다.

</details>

<details>
    <summary>@NotNull과 @Column(nullable = false)</summary>

* 두 값 모두 DDL에 속성을 넣어줄 수 있음
* @NotNull 어노테이션이 예외 검출 순간이 더 빨라 추천한다.
* https://unluckyjung.github.io/jpa/2022/01/17/JPA-Notnull-Column/
</details>


<details>
    <summary>cascade = CascadeType.ALL와 orphanRemoval = true </summary>

* CascadeType.REMOVE와 orphanRemoval = true는 부모 엔티티를 삭제하면 자식 엔티티도 삭제한다.
* 부모 엔티티에서 자식 엔티티를 삭제 시에는 CascadeType.REMOVE는 자식 엔티티가 그대로 남아있는 반면, orphanRemoval = true는 자식 엔티티를 제거한다.
* https://tecoble.techcourse.co.kr/post/2021-08-15-jpa-cascadetype-remove-vs-orphanremoval-true/

</details>

<details>
    <summary>RoomService 테스트 코드 작성 시 스프링테스트 없이 만들어보기</summary>

* 테스트
* repository 모킹

</details>

<details>
    <summary>No default constructor for entity 에러</summary>

* 엔티티에 기본 생성자가 없다는 에러이다.
* Hibernate가 엔티티를 생성하려고 할 때 기본 생성자를 호출하게 됩니다.(https://stackoverflow.com/questions/25452018/hibernate-annotations-no-default-constructor-for-entity?rq=1)
* 엔티티에 @NoArgsConstructor(access = AccessLevel.PROTECTED) 추가

org.hibernate.InstantiationException: No default constructor for entity

hibernate가 엔티티를 만들 때 기본 생성자를 이용하기 때문에 기본 생성자를 만들어줘야됨

</details>


<details>
    <summary>LazyInitializationException 발생</summary>

* Lazy로딩으로 발생한 이슈로 영속성 컨텍스트 종료된 후 엔티티에 참조 될 때 발생한다.
* 보통 서비스레이어에서 작업 후 그대로 반환하여 컨트롤러에서 dto로 변경될 때 발생됩니다. 프록시 객체를 가지고 있기때문입니다.
* 따라서 조회할 때 프록시 객체가 아닌 실제 객체가 들어갈 수 있게 fetch join으로 해결할 수 있습니다.

</details>



<details>
    <summary> java.lang.IllegalStateException: Cannot call sendError() after the response has been committed 발생</summary>

* 채팅방들을 조회하는 쿼리에서 발생한 이슈이다.
* Room들은 Participant List를 가지고 있고, 각 Participant는 Room을 가지고 있다.
* Room들을 조회하고 이를 그대로 반환하면 Room -> participant -> Room -> participant로 무한참조가 발생한다. (클라이언트에서 호출 시)
* 따라서 Json Ignore나 participants 엔티티를 그대로 반환하는 것이 아닌 그 안의 member이름만 따로 뽑아내서 dto를 만들어주고 반환하면 된다.
* Room들을 조회 중 Room에서 List로 participants를 가지고 있음 -> 그 안의 participant는 Room을 가지고 있음

</details>


<details>
    <summary>No serializer found for class com.hong.chatservice.room.application.ParticipantInfo and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) 에러</summary>

* dto로 클라이언트로 반환하는 중에 getter가 열려있지 않아서 발생하는 에러

</details>


<details>
    <summary>동시성 이슈 확인해보기</summary>

* public void checkEnoughHeadCount() { if ((participants.size() + 1) >= maxHeadCount) { throw new RuntimeException(); } }

</details>

