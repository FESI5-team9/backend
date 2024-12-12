## 서버 수동 재실행
```shell
 sudo systemctl start mukitlist-prod #서버 실행
 sudo systemctl stop mukitlist-prod #서버 종료
 sudo systemctl restart mukitlist-pord #서버 재실행
```

## 자동 재실행 스크립트 제거/추가
```shell
 sudo systemctl disable mukitlist-prod # 재실행 스크립트 제거
 sudo systemctl enable mukitlist-prod # 재실행 스크립트 추가

```