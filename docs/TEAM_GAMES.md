# 팀 게임 등록표

허브 앱에 연결할 게임 정보를 팀원이 여기에 먼저 적고, `GameInfo.kt` / `AndroidManifest.xml`에 반영합니다.

| 슬롯 | 게임명 | 담당 | applicationId (패키지명) | 저장소 URL | 상태 |
|------|--------|------|---------------------------|------------|------|
| 1 | (미정) | | `com.capstone.game1` | | 대기 |
| 2 | (미정) | | `com.capstone.game2` | | 대기 |
| 3 | (미정) | | `com.capstone.game3` | | 대기 |
| 4 | (미정) | | `com.capstone.game4` | | 대기 |

## 팀원이 제출할 정보

1. **applicationId** — `build.gradle`의 `applicationId` (확정 후 변경 금지 권장)
2. **게임 제목·한 줄 설명** — 허브 카드 UI용
3. **담당자 이름**
4. **(선택) 게임 저장소 URL** — 개별 게임은 별도 repo 권장

## 반영 체크리스트 (허브 담당자)

- [ ] `docs/TEAM_GAMES.md` 표 업데이트
- [ ] `app/.../model/GameInfo.kt` 수정
- [ ] `app/src/main/AndroidManifest.xml` `<queries>` 수정
- [ ] 실기기에서 4게임 「설치됨」 및 실행 확인
