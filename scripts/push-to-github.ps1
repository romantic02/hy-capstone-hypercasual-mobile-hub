# HY 캡스톤 통합 허브 — GitHub 최초 업로드 스크립트
# 사용: PowerShell에서 이 파일 실행 (관리자 권한 불필요)
#   cd D:\_03.HYU\_02.capstone\hy-hypercasual-mobile-hub
#   .\scripts\push-to-github.ps1

$ErrorActionPreference = "Stop"
$RepoName = "hy-capstone-hypercasual-mobile-hub"
$Description = "HY 캡스톤 하이퍼캐주얼 모바일게임 통합 허브"
$Root = Split-Path $PSScriptRoot -Parent

Set-Location $Root
Write-Host "Working directory: $Root"

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    throw "git이 설치되어 있지 않습니다."
}
if (-not (Get-Command gh -ErrorAction SilentlyContinue)) {
    throw "GitHub CLI(gh)가 설치되어 있지 않습니다. https://cli.github.com/"
}

gh auth status
if ($LASTEXITCODE -ne 0) {
    throw "gh 로그인이 필요합니다. 'gh auth login' 실행 후 다시 시도하세요."
}

if (-not (Test-Path .git)) {
    git init
    git branch -M main
}

git add .
git status --short

$commitCount = 0
try { $commitCount = [int](git rev-list --count HEAD 2>$null) } catch {}
if ($commitCount -eq 0) {
    git commit -m "Initial commit: HY capstone hypercasual mobile game hub"
}

$remoteUrl = git remote get-url origin 2>$null
if (-not $remoteUrl) {
    Write-Host "Creating GitHub repo: $RepoName"
    gh repo create $RepoName --public --source=. --remote=origin --description $Description --push
} else {
    Write-Host "Remote exists: $remoteUrl"
    git push -u origin main
}

$url = gh repo view --json url -q .url
Write-Host ""
Write-Host "Done! Repository URL:"
Write-Host $url
