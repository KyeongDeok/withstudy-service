[![HitCount](http://hits.dwyl.com/wyjax/withstudy.svg)](http://hits.dwyl.com/wyjax/withstudy)

 WITH STUDY (위드 스터디) - 프로젝트 스터디 팀빌딩
=

1.개발배경
--------------------
취업을 하기 위해서 포트폴리오가 필요했고 개인 프로젝트는 여러번 진행했으나 팀 프로젝트를 같이할 
사람이 없었다. -> 그래서 같이 나같은 사람들이 모일 수 있는 웹서비스를 개발하면 좋겠다고 생각

2.사용기술
--------------------
- 서버
    - Spring boot 2.1.9 (Gradle)
    - Spring JPA
    - Spring OAuth2
    - Spring Session
    
- 서바사이드 엔진
    - Thymeleaf       
     
- 프론트 엔드
    - html/css/javascript
    - JQuery
    - bootstrap
    - WSYWIG 
    
- 데이터베이스
    - MySQL 5.7
    
- OS
    - MAC OS Catalina    
    
- 툴
    - IntelliJ 2019.03
    - Workbench 8.0.19
    
## 3. 구조

```
.
└── com
    └── wyjax
        └── withstudy
            ├── InitDB.java
            ├── WithstudyApplication.java
            ├── config
            │   ├── HtmlCharacterEscapes.java
            │   ├── JpaConfig.java
            │   ├── WebMvcConfig.java
            │   └── auth
            │       ├── SecurityConfig.java
            │       ├── dto
            │       │   ├── OAuthAttribute.java
            │       │   └── SessionUser.java
            │       └── service
            │           └── CustomOAuth2UserService.java
            └── web
                ├── common
                │   ├── controller
                │   │   ├── CustomErrorController.java
                │   │   └── HomeController.java
                │   └── domain
                │       ├── BaseTimeEntity.java
                │       └── TimeConvert.java
                ├── content
                │   ├── controller
                │   │   ├── ContentAPIController.java
                │   │   └── ContentController.java
                │   ├── dto
                │   │   ├── ContentRequestDto.java
                │   │   └── ContentResponseDto.java
                │   ├── model
                │   │   ├── Content.java
                │   │   └── repository
                │   │       └── ContentRepository.java
                │   └── service
                │       └── ContentService.java
                ├── member
                │   ├── controller
                │   │   ├── MemberAPIController.java
                │   │   └── MemberController.java
                │   ├── dto
                │   │   ├── MemberRequestDto.java
                │   │   └── MemberResponseDto.java
                │   ├── model
                │   │   ├── Member.java
                │   │   ├── MemberRepository.java
                │   │   └── Role.java
                │   ├── portfolio
                │   │   ├── dto
                │   │   │   ├── PortFolioReqDto.java
                │   │   │   └── PortFolioResDto.java
                │   │   └── model
                │   │       ├── PortFolio.java
                │   │       └── PortFolioRepository.java
                │   └── service
                │       └── MemberService.java
                ├── push
                │   ├── controller
                │   │   ├── PushAPIController.java
                │   │   └── PushController.java
                │   ├── dto
                │   │   ├── PushRequestDto.java
                │   │   └── PushResponseDto.java
                │   ├── model
                │   │   ├── Push.java
                │   │   ├── UserChannel.java
                │   │   └── repository
                │   │       └── PushRepository.java
                │   └── service
                │       └── PushService.java
                └── study
                    ├── TechStack.java
                    ├── TechStackRepository.java
                    ├── controller
                    │   ├── StudyAPIController.java
                    │   └── StudyController.java
                    ├── dto
                    │   ├── StudyForm.java
                    │   ├── StudyRequestDto.java
                    │   ├── StudyRoomDesDto.java
                    │   └── StudyRoomDto.java
                    ├── model
                    │   ├── StudyMember.java
                    │   ├── StudyRequest.java
                    │   ├── StudyRoom.java
                    │   ├── StudyStatus.java
                    │   └── repository
                    │       ├── StudyMemberRepository.java
                    │       ├── StudyRepository.java
                    │       └── StudyRequestRepository.java
                    └── service
                        └── StudyService.java
```


## 4. 기능   

- 메인화면
<img width="1276" alt="main" src="https://user-images.githubusercontent.com/38483918/80821004-e7887080-8c12-11ea-90d4-4c19c37dde48.png">


- 스터디 목록
<img width="1265" alt="studyList" src="https://user-images.githubusercontent.com/38483918/80821153-30d8c000-8c13-11ea-95f5-19628d6f4eef.png">


- 스터디 상세보기
<img width="1264" alt="studyView" src="https://user-images.githubusercontent.com/38483918/80821163-32a28380-8c13-11ea-9393-76125975fe7f.png">


- 스터디 신청
<img width="1278" alt="studyRequest" src="https://user-images.githubusercontent.com/38483918/80821292-6382b880-8c13-11ea-9bea-d285fd86d203.png">


- 사용자 페이지 (닉네임 수정, 포트폴리오 추가 기능)
<img width="1263" alt="userinfo" src="https://user-images.githubusercontent.com/38483918/80821238-4f3ebb80-8c13-11ea-85ce-48c3740ce987.png">


- 사용자 알람

<img width="514" alt="alert" src="https://user-images.githubusercontent.com/38483918/80821242-506fe880-8c13-11ea-96c1-9522e1532e0b.png">