# 팀 게임 등록표

허브 앱에 연결할 게임 정보를 팀원이 여기에 먼저 적고, `GameInfo.kt` / `AndroidManifest.xml`에 반영합니다.

| 슬롯 | 게임명 | 담당 | applicationId (패키지명) | 저장소 URL | 상태 |
|------|--------|------|---------------------------|------------|------|
| 1 | Tap Blast: Drop & Merge | 본인 | `com.tapblast.dropmerge` | `Tap-Blast-Drop-Merge` | 테스트 중 |
| 2 | Chrono Cat | 팀원 | `com.hyhyper.chronocat` | | 연동됨 |
| 3 | Slime Dash 3D | 팀원 | `com.DefaultCompany.SlimeDash3D` | | 연동됨 |
| 4 | Sword Rush: Hold Action | 팀원 | `com.MySWCapstone.Sword_Rush` | | 연동됨 |

## 팀원이 제출할 정보

1. **applicationId** — `build.gradle`의 `applicationId` (확정 후 변경 금지 권장)
2. **게임 제목·한 줄 설명** — 허브 카드 UI용
3. **담당자 이름**
4. **(선택) 게임 아이콘** — PNG **1024×1024, 여백 없이 꽉 찬 정사각형**, 투명 배경은 아이콘 실루엣 밖만 → `drawable/ic_game_슬롯.png`
5. **(선택) 게임 저장소 URL** — 개별 게임은 별도 repo 권장

### 아이콘 등록 (허브 담당자)

1. PNG를 `drawable/`에 넣기 (파일명: 소문자·숫자·밑줄만, 예: `ic_game_tap_blast.png`)
2. `GameInfo.kt` 해당 슬롯에 `iconResId = R.drawable.ic_game_tap_blast` 추가
3. 아이콘 없으면 번호 배지(기본)로 표시됨

## 반영 체크리스트 (허브 담당자)

- [ ] `docs/TEAM_GAMES.md` 표 업데이트
- [ ] `app/.../model/GameInfo.kt` 수정
- [ ] `app/src/main/AndroidManifest.xml` `<queries>` 수정
- [x] 실기기에서 4게임 「설치됨」 및 실행 확인
