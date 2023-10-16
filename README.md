# diary-board
diary-board 프로젝트의 백엔드 Repository입니다.  
  
👉 [프론트엔드 Repository](https://github.com/likeyeon/diary-board)
## 1. 프로젝트 소개
일기장을 공유하고 게시할 수 있는 커뮤니티 서비스입니다.  
  
### 팀원 
- 조하연(FE)    
- 황영훈(BE)  

### 개발 방법 
- 1주동안 스프린트를 진행합니다.  
- 매주 월요일 3시에 스프린트 회고를 합니다.  

### 개발 기간
- 9월 4일 (월) ~ 10월 16일 (월)
- 수시로 리팩토링 진행중 

### 참고
- [스프린트 블로그](https://philosophy-coding.tistory.com/40)  
- [API 문서](https://dairy-board.gitbook.io/api)

## 2. 기술 스택
- Java 17
- SpringBoot 3.1.3
- Spring Data JPA
- Spring Security OAuth2 Resource Server 6.1.3
- H2 2.1.214 (추후 MySQL로 변경 예정)

## 3. 주요 기능
### 회원
- JWT를 이용한 토큰 인증
- 게시글 작성 등 권한이 필요한 API 호출 시 사용
### 게시글
- 게시글 CRUD 구현
- ModelMapper 라이브러리를 활용해서 PATCH 메소드로 Update
- 필드별 검색, 날짜별 조회, 정렬 기능 구현
### 댓글 및 좋아요
- 게시글에 댓글과 좋아요 생성 가능
- 댓글에 좋아요 생성 가능
- 회원이 좋아요를 눌렀는지 확인하는 로직 구현

## 4. 트러블 슈팅
### @OneToMany와 Fetch Join 문제
- 일대다 필드가 하나라면 fetch join으로 N+1 문제를 해결할 수 있습니다.
- 하지만 두 개의 일대다 필드를 조회할 때는 fetch join으로 가져올 수 없습니다. (JPA 표준 위반)
- 조회 결과가 카테시안 곱으로 반환되어 데이터 중복문제가 발생하기 때문입니다.
- Batch Size 옵션을 적용해서 상수번 쿼리를 날리도록 수정하여 문제를 해결했습니다.
### 댓글 좋아요 눌렀는지 확인하기 성능 문제
- 기존에는 댓글마다 좋아요 컬렉션을 메모리로 가져온 후 Stream 연산을 통해 좋아요를 눌렀는지 확인했습니다.
- 하지만 댓글과 좋아요의 개수가 많을 경우, 불필요한 메모리 부하와 Stream 연산 비용이 발생합니다.
- 다음 방법으로 문제를 해결했습니다.
  - 회원 ID와 댓글 IDs를 이용해서 좋아요 컬렉션을 한 번 조회합니다.
  - 좋아요 누른 댓글 IDs만 Set으로 바꿉니다.
  - 댓글마다 contains 메소드를 이용해서 좋아요 눌렀는지 여부를 확인합니다.
## 5. 회고

