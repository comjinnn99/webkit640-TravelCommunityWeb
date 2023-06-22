# webkit640-TravelCommunityWeb
국내 여행 커뮤니티 웹. 개인프로젝트
웹킷640 과정

# 인텔리제이 clone 시 에러
> Could not resolve all files for configuration ':classpath'.
   > Could not resolve org.springframework.boot:spring-boot-gradle-plugin:3.0.0.

위와 같은 에러가 뜨는 경우
-> 프로젝트는 jdk 17 버전을 필요로 함

- main menu -> project structure -> sdk 부분을 17로 변경

그래도 같은 이슈 발생?

- main menu -> settings -> build, execution, deployment -> build tools -> gradle -> gradle jvm 을 17로 변경
