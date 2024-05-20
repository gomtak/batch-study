 ### kotlin / spring boot 를 사용한 배치 프로젝트
 * git branch 로 버전별로 관리. 
 * java 17 이상
 * sql 하위 폴더에 sql 파일 실행
   * /org/springframework/batch/core/schema-**.sql 존재
 * 데이터베이스 실제 로그 확인하기
   * show variables like 'general_log%';
   * set global general_log = 'ON';
---
 ### Branch
 - [x] simple job
 - [x] multiple job
 - [x] jpaItemReader, jpaItemWriter
 - [x] job parameter 