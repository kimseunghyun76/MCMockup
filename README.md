# MCMockup

급작스럽게 만들어 본 프로그램

우선 Spring boot 로 골격을 만들어 봅니다.
여기에서, 클라이언트 측에서 Rest 통신(Ajax 이용)해서, 데이터를 받아서 클라이언트 측에 바로 뿌려줄 예정입니다.

아래 사이트에 아주 디테일하게 설명이 잘 되어 있으니 참고하시길 바랍니다. 
https://www.mkyong.com/spring-boot/spring-boot-ajax-example/

여기서는 maven을 사용했으나, 나는 gradle 를 이용해 봅니다.

그리고, spring-boot-devtools 은..
개발시에 정적인 파일들 수정 후 확인시에 계속 서버를 재기동 해야하는 불편함때문에 사용되는 것으로 이해 하면 됩니다. 
https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html

Spring Boot는 어플리케이션 개발 경험을 좀더 쾌적하게 만들어 줄수 있는 추가적인 툴을 포함하고 있습니다.
spring-boot-devtools 모듈은 추가적은 development-time 기능들을 제공하는 어떤 프로젝트에도 포함될수 있습니다.
개발툴 지원을 포함하기 위해서, 간단하게 모듈 디펜던시를 추가하면 됩니다.
