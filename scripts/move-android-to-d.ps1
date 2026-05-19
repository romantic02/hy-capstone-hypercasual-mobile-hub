# Android SDK / AVD / Gradle 캐시를 D:로 이동하고 C:에는 junction(연결) 유지
# 관리자 권한 불필요. Android Studio·에뮬레이터는 종료 후 실행하세요.
# 사용: PowerShell에서 .\scripts\move-android-to-d.ps1

$ErrorActionPreference = "Stop"
$DestRoot = "D:\_06.ProgramFiles\Android"

$moves = @(
    @{ Source = "$env:LOCALAPPDATA\Android"; Target = "$DestRoot\AppData-Local-Android"; Link = "$env:LOCALAPPDATA\Android" },
    @{ Source = "$env:USERPROFILE\.android"; Target = "$DestRoot\dot-android"; Link = "$env:USERPROFILE\.android" },
    @{ Source = "$env:USERPROFILE\.gradle"; Target = "$DestRoot\dot-gradle"; Link = "$env:USERPROFILE\.gradle" }
)

function Move-AndLink($Source, $Target, $Link) {
    if (-not (Test-Path $Source)) {
        Write-Host "SKIP (없음): $Source"
        return
    }
    if ((Get-Item $Source -Force).Attributes -band [IO.FileAttributes]::ReparsePoint) {
        Write-Host "SKIP (이미 junction): $Source"
        return
    }

    New-Item -ItemType Directory -Path (Split-Path $Target) -Force | Out-Null
    if (Test-Path $Target) {
        throw "대상이 이미 있습니다: $Target"
    }

    Write-Host "이동 중: $Source -> $Target"
    robocopy $Source $Target /E /MOVE /R:1 /W:1 /NFL /NDL /NJH /NJS | Out-Null
    if ($LASTEXITCODE -ge 8) { throw "robocopy 실패: $Source (code $LASTEXITCODE)" }

    if (Test-Path $Source) { Remove-Item $Source -Recurse -Force -ErrorAction SilentlyContinue }
    if (Test-Path $Link) { Remove-Item $Link -Force -ErrorAction SilentlyContinue }

    cmd /c "mklink /J `"$Link`" `"$Target`""
    if ($LASTEXITCODE -ne 0) { throw "junction 생성 실패: $Link" }
    Write-Host "OK: $Link -> $Target"
}

Write-Host "=== Android 데이터 D: 이동 ==="
New-Item -ItemType Directory -Path $DestRoot -Force | Out-Null

foreach ($m in $moves) {
    Move-AndLink $m.Source $m.Target $m.Link
}

# Gradle 환경 변수 (새 터미널/Studio부터 적용)
[Environment]::SetEnvironmentVariable("GRADLE_USER_HOME", "$env:USERPROFILE\.gradle", "User")
[Environment]::SetEnvironmentVariable("ANDROID_HOME", "$env:LOCALAPPDATA\Android\Sdk", "User")
[Environment]::SetEnvironmentVariable("ANDROID_SDK_ROOT", "$env:LOCALAPPDATA\Android\Sdk", "User")

Write-Host ""
Write-Host "완료. Android Studio를 다시 실행하세요."
Write-Host "SDK 경로(변경 없음): $env:LOCALAPPDATA\Android\Sdk"
