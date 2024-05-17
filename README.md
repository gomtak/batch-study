### kotlin / spring boot 를 사용한 배치 프로젝트
* java 17 이상
* sql 하위 폴더에 sql 파일 실행
  * /org/springframework/batch/core/schema-**.sql 존재
* 잡이 두개 이상일시 아래 설정이 필요함.
````yml
spring:
 batch:
  job:
   name: ${job.name:job}
````
* program arguments: --job.name=jobName 로 진행