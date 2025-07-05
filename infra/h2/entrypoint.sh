#!/bin/sh
DATA_DIR=/opt/h2-data

if ! ls "$DATA_DIR"/test.mv.db; then
  echo "데이터베이스 초기 설정 실행.. /sql/init.sql 실행"
  java -cp /opt/h2/bin/h2*.jar org.h2.tools.RunScript \
    -url "jdbc:h2:file:$DATA_DIR/test" \
    -user sa \
    -script /sql/init.sql
  echo "초기 설정 실행 완료"
  
  echo "Batch 테이블 추가.. /sql/batch-h2.sql 실행"
  java -cp /opt/h2/bin/h2*.jar org.h2.tools.RunScript \
    -url "jdbc:h2:file:$DATA_DIR/test" \
    -user sa \
    -script /sql/batch-h2.sql
  echo "초기 설정 실행 완료"
fi

# H2 서버 실행
exec java -cp /opt/h2/bin/h2*.jar org.h2.tools.Server \
  -tcp -tcpAllowOthers \
  -web -webAllowOthers \
  -baseDir "$DATA_DIR" \
  -ifNotExists
