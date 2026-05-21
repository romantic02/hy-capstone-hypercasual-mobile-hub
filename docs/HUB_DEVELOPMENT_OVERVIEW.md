# HY 캡스톤 하이퍼캐주얼 모바일게임 통합 허브 — 개발 개요

> **문서 목적**  
> 통합 허브 애플리케이션의 설계 의도, 기술 구조, 구현 방식, 팀 협업 절차를 기록한다.  
> **대상 독자:** 프로젝트 이해관계자, 게임 개발 팀원, 발표·산출물 검토자  
> **최종 갱신:** 2026년 5월 (4게임 연동·Release `hub-v1.0.0` 기준)

---

## 1. Executive Summary

본 프로젝트는 한양대학교 캡스톤 디자인 과정에서 제작된 **4종의 독립 Android 하이퍼캐주얼 게임**을, 단일 진입점(통합 허브)에서 순차적으로 시연·실행할 수 있도록 하는 **모바일 게임 런처(Launcher Hub)** 이다.

허브 애플리케이션은 게임 로직을 포함하지 않으며, Android 플랫폼의 **패키지 기반 앱 실행(Intent)** 메커니즘을 통해 각 팀원이 빌드한 게임 APK와 연동한다. UI는 디자인 산출물(Figma)을 기준으로 구현하였고, 게임 메타데이터(패키지명, 표시명, 아이콘)는 설정 파일 수준에서 관리하여 유지보수 비용을 최소화하였다.

| 항목 | 내용 |
|------|------|
| 허브 패키지명 | `com.capstone.hub` |
| 기술 스택 | Kotlin, Jetpack Compose, Material 3 |
| 최소 지원 OS | Android 8.0 (API 26) |
| 연동 게임 수 | 4 |
| 배포 | GitHub Releases (`hub-v1.0.0`) |

---

## 2. 프로젝트 배경 및 목표

### 2.1 배경

팀원 4인이 각각 Unity 기반 모바일 게임을 개발함에 따라, 발표 및 시연 환경에서는 **다수의 APK 설치**와 **빠른 게임 전환**이 필요하다. 개별 게임을 홈 화면에서 찾아 실행하는 방식은 시연 흐름을 저해하므로, 브랜드·레이아웃이 통일된 **전용 허브 화면**을 통해 4개 게임에 접근하는 구조를 채택하였다.

### 2.2 목표

1. **단일 UX 진입점** — 발표용 기기에서 허브 한 화면으로 4게임 실행  
2. **게임 코드 분리** — 허브와 게임 빌드·배포 파이프라인 독립 유지  
3. **디자인 정합성** — Figma 시안(`image_hubmain`)과 시각적 일치  
4. **운영 단순성** — 게임 추가·변경 시 소스 수정 범위 최소화(명단·manifest·리소스)

### 2.3 비목표 (Out of Scope)

- 게임 소스코드의 허브 프로젝트 내 통합  
- 백엔드 서버, 계정·랭킹 등 온라인 연동  
- iOS 플랫폼 지원  
- 스토어 배포용 서명·릴리즈 파이프라인 자동화(현 단계는 팀 내부 debug APK 배포)

---

## 3. 시스템 아키텍처

### 3.1 논리 구조

허브와 각 게임은 **프로세스·APK 단위로 완전히 분리**된다. 허브는 게임 실행 시 Android OS에 **명시적 실행 요청(Intent)** 만 전달한다.

```
┌──────────────────────────────────────────┐
│  Capstone Hub (com.capstone.hub)         │
│  · UI: 2×2 게임 그리드                   │
│  · GameCatalog (정적 메타데이터)          │
│  · GameLauncher (설치 확인 + Intent)      │
└──────────────────┬───────────────────────┘
                   │ startActivity(launchIntent)
         ┌─────────┼─────────┬─────────────┐
         ▼         ▼         ▼             ▼
    [Game 1 APK] [Game 2] [Game 3]    [Game 4]
    Unity 빌드   Unity     Unity       Unity
```

### 3.2 연동 계약 (Integration Contract)

게임 측이 만족해야 하는 조건은 다음과 같다.

| 요구사항 | 설명 |
|----------|------|
| 고유 `applicationId` | Unity Android 빌드 설정과 허브 `GameInfo.packageName` **완전 일치** |
| LAUNCHER Activity | `MAIN` + `LAUNCHER` 인텐트 필터가 있는 시작 Activity 존재 |
| APK 설치 | 시연 기기에 허브(1) + 게임(4) **총 5개 APK** 설치 |

허브는 게임 내부 API를 호출하지 않는다. **패키지명 일치**가 유일한 런타임 결합 키이다.

### 3.3 Android 11+ 패키지 가시성

Android 11(API 30) 이상에서는 다른 앱 패키지 조회가 제한된다. 허브 `AndroidManifest.xml`의 `<queries>` 블록에 연동 대상 패키지명을 선언하여, `PackageManager`를 통한 설치 여부 확인 및 launch intent 획득이 가능하도록 구성하였다.

---

## 4. 기술 스택 및 선정 근거

| 계층 | 기술 | 선정 근거 |
|------|------|-----------|
| 언어 | Kotlin | Android 공식 권장, Compose와의 상호운용 |
| UI | Jetpack Compose | 선언적 UI, Figma 기반 화면 구현·수정 효율 |
| 디자인 시스템 | Material 3 | Snackbar, Scaffold 등 플랫폼 표준 피드백 |
| 빌드 | Gradle 8.11, AGP 8.7 | Android Studio 및 CLI 빌드 호환 |
| 게임 엔진 | (허브 외부) Unity | 팀원 개별 프로젝트 |

허브 `applicationId`: **`com.capstone.hub`**  
`minSdk`: **26**, `targetSdk`: **35**

---

## 5. 소프트웨어 구조

### 5.1 패키지 구성

```
com.capstone.hub/
├── MainActivity.kt          # 앱 진입, Compose 루트
├── model/
│   └── GameInfo.kt          # 게임 메타데이터·GameCatalog
├── launcher/
│   └── GameLauncher.kt      # 설치 확인, Intent 실행, 결과 enum
└── ui/
    ├── HubScreen.kt         # 메인 UI, 그리드, Snackbar
    └── theme/
        ├── HubColors.kt     # Figma 색상 토큰
        ├── HubTypography.kt # Bagel Fat One 폰트
        └── Theme.kt         # Material Theme
```

### 5.2 핵심 모듈 역할

**GameCatalog (`GameInfo.kt`)**  
4개 슬롯에 대한 정적 목록. 각 항목은 슬롯 ID, 표시 제목, 패키지명, 강조 색상, drawable 아이콘 리소스를 보유한다.

**GameLauncher (`GameLauncher.kt`)**  
- `isInstalled`: `PackageManager.getPackageInfo` 기반 설치 여부 판별  
- `launch`: `getLaunchIntentForPackage` → `startActivity`  
- 반환값 `LaunchResult`: `Success` | `NotInstalled` | `Failed`

**HubScreen (`HubScreen.kt`)**  
- Figma 레이아웃: 상단 브랜드 헤더, 중앙 2×2 그리드, 게임별 아이콘·제목  
- 탭 이벤트 → `GameLauncher.launch`  
- 미설치·실패 시 Material **Snackbar** (스와이프·닫기·자동 소멸)

### 5.3 UI/UX 구현 요약

| UI 요소 | 구현要点 |
|---------|----------|
| 전역 배경 | `#FFE066` (Figma yellow) |
| 헤더 pill | `#FF5E5B`, 하단 4px 오프셋 그림자 `#C33B39`, 폰트 Bagel Fat One |
| 게임 슬롯 | 2×2, 검정 테두리·오프셋 그림자, 슬롯별 accent color |
| 제목 | `\n` 기준 수동 줄바꿈 지원, 좁은 폭에서 자동 글자 크기 축소 |
| 피드백 | Toast 대신 Snackbar (시연 중 메시지 잔류 문제 해소) |

상태바·내비게이션 바 인셋(`WindowInsets`)을 적용하여 실제 기기 노치·제스처 영역과 겹치지 않도록 처리하였다. Figma 장식용 노치·하단 네비게이션 바는 시연 요구에 따라 제거하였다.

---

## 6. 게임 연동 현황

| 슬롯 | 표시명 | applicationId | 아이콘 리소스 |
|------|--------|---------------|---------------|
| 1 | Tap Blast: Drop & Merge | `com.tapblast.dropmerge` | `ic_game_tap_blast` |
| 2 | Chrono Cat | `com.hyhyper.chronocat` | `ic_game_slot2` |
| 3 | Slime Dash 3D | `com.DefaultCompany.SlimeDash3D` | `ic_game_slot3` |
| 4 | Sword Rush: Hold Action | `com.MySWCapstone.Sword_Rush` | `ic_game_slot4` |

등록 절차 및 체크리스트는 `docs/TEAM_GAMES.md`에 정의되어 있다.

---

## 7. 개발 프로세스

### 7.1 단계별 이력

1. **프로토타입** — 2×2 카드 UI, Intent 기반 실행, 미설치 피드백  
2. **1차 연동 검증** — Tap Blast 실기기 패키지·아이콘 반영  
3. **저장소·협업 기반** — GitHub repo, 팀 등록 문서, device 스크립트  
4. **UI 고도화** — Figma `image_hubmain` 토큰·타이포·레이아웃 반영  
5. **UX 정제** — Snackbar, 제목 줄바꿈·폰트 스케일, safe area  
6. **전 게임 등록** — 슬롯 2~4 메타데이터·manifest·drawable 반영  
7. **배포** — Gradle Wrapper, `assembleDebug`, GitHub Release `hub-v1.0.0`

### 7.2 협업 모델

| 역할 | 책임 |
|------|------|
| 허브 개발 | UI, GameCatalog, manifest queries, APK 빌드·Release |
| 게임 개발 (×4) | Unity 게임, applicationId 확정, APK·아이콘·표시명 제출 |
| 디자인 | Figma 메인 화면, 게임 아이콘 가이드 |

게임 변경 시 허브 측 수정 파일은 **3곳**으로 한정된다: `GameInfo.kt`, `AndroidManifest.xml`, `res/drawable/`.

---

## 8. 빌드 및 배포

### 8.1 로컬 빌드

```powershell
cd hy-hypercasual-mobile-hub
.\gradlew.bat assembleDebug
```

산출물: `app/build/outputs/apk/debug/app-debug.apk`

### 8.2 팀 배포

- **소스 코드:** https://github.com/romantic02/hy-capstone-hypercasual-mobile-hub  
- **설치 APK:** https://github.com/romantic02/hy-capstone-hypercasual-mobile-hub/releases/tag/hub-v1.0.0  

APK는 저장소에 커밋하지 않으며(`.gitignore`), Release asset으로만 배포한다.

### 8.3 시연 기기 준비 체크리스트

- [ ] 허브 APK 설치  
- [ ] 게임 APK 4종 설치  
- [ ] 각 슬롯 탭 시 정상 기동 확인  
- [ ] 미설치 슬롯 Snackbar 문구 확인  

---

## 9. 제한 사항 및 향후 과제

### 9.1 현재 제한

- **게임 복귀:** 허브에서 게임 실행 후, 시스템 뒤로가기만으로 허브 복귀는 보장되지 않음. 게임 앱에서 허브 패키지를 명시적으로 실행하는 처리가 필요할 수 있음.  
- **단일 빌드 variant:** 팀 공유는 debug APK 기준. Play 스토어 배포용 서명·ProGuard 미적용.  
- **동적 목록:** 게임 목록은 코드 내 정적 정의이며, 원격 설정·OTA 업데이트 없음.

### 9.2 향후 개선 가능 항목

- Release 서명 및 CI/CD(GitHub Actions) 자동 빌드  
- 게임별 설치 여부 UI 표시(배지)  
- 공통 뒤로가기·허브 복귀 규약을 게임 템플릿에 문서화  
- `applicationId` 변경 시 검증 스크립트

---

## 10. 참고 자료

| 자료 | 위치 |
|------|------|
| 팀 게임 등록표 | `docs/TEAM_GAMES.md` |
| 프로젝트 README | `README.md` |
| GitHub 저장소 | https://github.com/romantic02/hy-capstone-hypercasual-mobile-hub |
| APK Release | https://github.com/romantic02/hy-capstone-hypercasual-mobile-hub/releases/tag/hub-v1.0.0 |

---

## 부록 A. 비개발 이해관계자용 요약

통합 허브는 **게임 4개를 한 화면에서 고르는 바로가기 앱**이다. 게임 본체는 각 팀원이 만든 **별도 앱**이며, 허브는 **실행만 연결**한다. 발표 폰에는 **앱 5개(허브 1 + 게임 4)** 가 설치되어 있어야 한다. 화면 디자인은 Figma를 따랐고, 팀원이 준 **이름·아이콘·패키지명**을 명단에 등록해 연동하였다.

## 부록 B. 게임 개발 팀원용 요약

허브는 Unity 게임 코드를 import하지 않는다. 연동에 필요한 것은 **Android `applicationId` 일치**, **LAUNCHER Activity**, **APK 설치**뿐이다. 패키지명·제목·아이콘을 허브 담당자에게 전달하면 `GameInfo.kt`와 manifest에 반영된다. 허브 APK는 GitHub Release에서 받아 설치한다.

---

*본 문서는 HY 캡스톤 하이퍼캐주얼 모바일게임 통합 허브 프로젝트의 기술·운영 문서이며, Notion 등 협업 도구에 그대로 이식하여 사용할 수 있다.*
