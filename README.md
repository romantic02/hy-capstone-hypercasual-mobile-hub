# HY 캡스톤 하이퍼캐주얼 모바일게임 통합 허브

**프로젝트 경로:** `D:\_03.HYU\_02.capstone\hy-hypercasual-mobile-hub`

4명의 팀원이 각각 만든 **독립 Android 하이퍼캐주얼 게임 APK**를 발표 때 한 화면에서 실행하는 통합 런처(허브) 앱입니다.

## GitHub 저장소 이름 (권장)

| 용도 | 이름 |
|------|------|
| **표시 이름** (GitHub Description, README 제목) | `HY 캡스톤 하이퍼캐주얼 모바일게임 통합 허브` |
| **저장소 slug** (URL, clone 경로) | `hy-capstone-hypercasual-mobile-hub` |

한글 제목은 포트폴리오·발표에 좋고, **저장소 URL은 영문·하이픈**이 표준입니다.

```text
https://github.com/YOUR_USER/hy-capstone-hypercasual-mobile-hub
```

## 동작 방식

```
[통합 허브 APK]  ──Intent──▶  [게임1 APK]
              ──Intent──▶  [게임2 APK]
              ──Intent──▶  [게임3 APK]
              ──Intent──▶  [게임4 APK]
```

## 팀원과 맞춰야 할 것

| 항목 | 설명 |
|------|------|
| **applicationId** | 각 게임과 `GameInfo.packageName`이 **완전히 동일** |
| **LAUNCHER Activity** | 각 게임에 `MAIN` + `LAUNCHER` 인텐트 필터 |
| **APK 설치** | 발표 기기에 게임 4개 + 허브 **총 5개** |

수정 파일:

1. `docs/TEAM_GAMES.md` — 팀 등록표
2. `app/src/main/java/com/capstone/hub/model/GameInfo.kt`
3. `app/src/main/AndroidManifest.xml` — `<queries>`

## 빌드

1. Android Studio에서 **이 폴더**(`hy-hypercasual-mobile-hub`)를 Open
2. Gradle Sync → Run
3. 팀원 APK 설치 후 허브에서 실행 확인

## Git 초기화 (이 폴더만 repo로 쓸 때)

```powershell
cd D:\_03.HYU\_02.capstone\hy-hypercasual-mobile-hub
git init
git add .
git commit -m "Initial commit: HY capstone hypercasual mobile game hub"
git branch -M main
git remote add origin https://github.com/YOUR_USER/hy-capstone-hypercasual-mobile-hub.git
git push -u origin main
```

## 기술 스택

Kotlin · Jetpack Compose · Material 3 · minSdk 26
