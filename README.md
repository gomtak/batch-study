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
- [x] exposed 로 배치 실행.


### Branch 설명
* kotlin, exposed 를 사용하여 배치 프로젝트를 만들기 위한 목적
* 브랜치별로 단계를 구분해서 진행