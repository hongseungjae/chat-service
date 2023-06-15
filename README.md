# Chat service
스프링부트 웹소켓을 이용한 채팅서비스

## 경험
### N:M 관계에서 1:N, M:1 관계로 풀기

* 채팅방(Room)과 유저(Member)의 관계는 다대다 입니다. 하나의 유저가 여러 채팅 방에 속해있을 수 있으며, 한 개의 방에는 여러명의 유저가 있게 됩니다.
JPA에서는 다대다 관계를 @ManyToMany를 이용해서 매핑 시킵니다. @ManyToMany를 사용하면 둘 테이블을 매핑시키기 위한 중간 테이블이 생깁니다. 
이 중간 테이블에는 다음과 같은 단점이 있습니다.
  1. 매핑정보만 들어가고 추가 데이터를 넣는 것이 불가능하다. 
  2. 중간 테이블이 숨겨져 있기 때문에 쿼리가 예상하지 못하는 형태로 나간다.
* 따라서 이런 문제를 해결하기 위해서는 중간에 엔티티를 따로 두는 방식으로 풀어낼 수 있습니다.

Member - Participant - Room 과 같은 관계로 풀어낼 수 있습니다.

* https://www.inflearn.com/questions/435090/%EB%8B%A4%EB%8C%80%EB%8B%A4-%EA%B4%80%EA%B3%84%EA%B0%80-%EC%95%88%EC%A2%8B%EC%9D%80-%EC%9D%B4%EC%9C%A0


### JPA의 Id값은 int나 long 기본자료형이 아닌 Long, Integer 참조형을 사용하는 이유
1. null을 가질 수 있습니다. 이 의미는 0으로 값을 의도한 것인지, 아니면 단순 값을 초기화 안한건지 파악할 수 있습니다. 

2. Validate에서 @NotNull 사용할 수 있습니다.

   - 그 외에 필드 값의 경우 null을 관리해야하는지에 따라 선택적으로 사용하면 된다.

### @NotNull과 @Column(nullable = false) 차이점
* 두 값 모두 DDL에 속성을 넣어줄 수 있음
* @NotNull 어노테이션이 예외 검출 순간이 더 빨라 추천한다.
* https://unluckyjung.github.io/jpa/2022/01/17/JPA-Notnull-Column/

https://unluckyjung.github.io/jpa/2022/01/17/JPA-Notnull-Column/

* cascade = CascadeType.ALL, orphanRemoval = true

* RoomService 테스트 코드 작성 시 스프링테스트 없이 만들어보기

* 컨트롤러에서 Room 전체 조회 시 2023-05-09T22:54:29.230+09:00 WARN 22392 --- [nio-8080-exec-4] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotWritableException: Could not write JSON: No default constructor for entity: : com.hong.chatservice.participant.domain.Participant]

Participant 엔티티에 @NoArgsConstructor(access = AccessLevel.PROTECTED) 추가

org.hibernate.InstantiationException: No default constructor for entity

hibernate가 엔티티를 만들 때 기본 생성자를 이용하기 때문에 기본 생성자를 만들어줘야됨

* base entity에 Serializable 하는 이유
2.No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer

class com.hong.chatservice.member.domain.Member$HibernateProxy$CIKOzJvT 이걸 그대로 반환 시 에러

LAZY로딩 발생하였는데 participant인줄 알았으나 participant안에 member였다

java.lang.IllegalStateException: Cannot call sendError() after the response has been committed 에러
Room들을 조회 중 Room에서 List로 participants를 가지고 있음 -> 그 안의 participant는 Room을 가지고 있음

그러면 Room -> participant -> Room -> participant로 무한참조 -> 클라이언트에서 호출할때만 발생

양방향관계이므로

dto 변환시 그대로 participants로 반환이 아닌 그 안에 member 이름만 출력하게

json ignore

No serializer found for class com.hong.chatservice.room.application.ParticipantInfo and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) 에러

dto로 나가는 것중에 getter가 열려있지 않아서

* 동시성 이슈 확인해보기 public void checkEnoughHeadCount() { if ((participants.size() + 1) >= maxHeadCount) { throw new RuntimeException(); } }

* disconnect가 발생하면
DISCONNECT
receipt:77
 
이거 밖에 없음, destination이 header가 있어야 추출해서 나갔따고 뿌릴수있느데 안됨

해결해보기 
1. message simpSessionId가 저장되어있음, 따라서 처음 들어올때 이 ID와 룸ID 매핑시켜서 db 저장

2. 웹소켓 세션에 저장
        accessor.getSessionAttributes().put("destination", destination);
        String destination = (String) accessor.getSessionAttributes().get("destination");

