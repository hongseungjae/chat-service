# chat-service
스프링부트 웹소켓을 이용한 채팅서비스



1. room 엔티티
2. room api
3. 멤버 security 로그인 - 일단 세션
4. room ui, 로그인 ui
5. 입장 시 

1. 다대다는 다대일 일대다
방과 멤버의 관계,  하나의 멤버가 여러개의 방에 속해있을 수 있고, 한 개의 방에 여러명의 멤버가 있음

풀기위해서 participant 

2. JPA int, Long?
long은 더 커서 , long -> Long은 null을 가질수있음,, int로 할시 초기화하면 0인데 생성된건지 0이들어간건지 알수없음
Long은 아님 -> 더찾아보기

3.@NotNull과 @Column(nullable = false) 
https://unluckyjung.github.io/jpa/2022/01/17/JPA-Notnull-Column/

4.
제가 추천하는 방법은 DTO에서 검증을 완료하고, 검증된 내용을 entity에 넣어주는 것입니다.
두번 검증?

5. 권한관련
방제를 바꾸는 권한이 어드민에게만 있는데, 이게 만약 유저에게도 권한을 주려면, 코드 수정을 해야되나?

6. cascade = CascadeType.ALL, orphanRemoval = true

5.09

7. RoomService 테스트 코드 작성 시 스프링테스트 없이 만들어보기

8. 
컨트롤러에서 Room 전체 조회 시
2023-05-09T22:54:29.230+09:00  WARN 22392 --- [nio-8080-exec-4] .w.s.m.s.DefaultHandlerExceptionResolver : Resolved [org.springframework.http.converter.HttpMessageNotWritableException: Could not write JSON: No default constructor for entity:  : com.hong.chatservice.participant.domain.Participant]

Participant 엔티티에
@NoArgsConstructor(access = AccessLevel.PROTECTED)
추가

방만들고 -> 방목록 뿌려보기

5.10

1. base entity에 Serializable 하는 이유

2.No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer

 class com.hong.chatservice.member.domain.Member$HibernateProxy$CIKOzJvT
이걸 그대로 반환 시 에러

LAZY로딩 발생하였는데 participant인줄 알았으나 participant안에 member였다

3. java.lang.IllegalStateException: Cannot call sendError() after the response has been committed 에러

Room들을 조회 중
Room에서 List로 participants를 가지고 있음 -> 그 안의 participant는 Room을 가지고 있음

그러면 Room -> participant -> Room -> participant로 무한참조 -> 클라이언트에서 호출할때만 발생

양방향관계이므로

1. dto 변환시 그대로 participants로 반환이 아닌 그 안에 member 이름만 출력하게
2. json ignore



3. No serializer found for class com.hong.chatservice.room.application.ParticipantInfo and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS)  에러

dto로 나가는 것중에 getter가 열려있지 않아서


=======
방제목 이름 중복 확인하기,

