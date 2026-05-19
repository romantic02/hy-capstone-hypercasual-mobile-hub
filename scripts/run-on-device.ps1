# 허브 앱을 Android Studio 없이 실기기/에뮬레이터에 설치·실행
# 사용: PowerShell에서 .\scripts\run-on-device.ps1

$ErrorActionPreference = "Stop"
$Root = Split-Path $PSScriptRoot -Parent
$Apk = Join-Path $Root "app\build\intermediates\apk\debug\app-debug.apk"
$Adb = Join-Path $env:LOCALAPPDATA "Android\Sdk\platform-tools\adb.exe"
$Package = "com.capstone.hub"
$Activity = "$Package/.MainActivity"

if (-not (Test-Path $Adb)) {
    throw "adb를 찾을 수 없습니다. Android SDK platform-tools를 설치하세요."
}

$devices = & $Adb devices | Select-String "device$"
if (-not $devices) {
    throw "연결된 기기/에뮬레이터가 없습니다. USB 디버깅 또는 에뮬레이터를 먼저 켜세요."
}

if (-not (Test-Path $Apk)) {
    Write-Host "APK가 없습니다. Android Studio에서 Build > Make Project 한 번 실행 후 다시 시도하세요."
    Write-Host "경로: $Apk"
    exit 1
}

Write-Host "설치 중..."
& $Adb install -r -t $Apk
Write-Host "실행 중..."
& $Adb shell am start -n $Activity
Write-Host "완료. 기기 화면을 확인하세요."
