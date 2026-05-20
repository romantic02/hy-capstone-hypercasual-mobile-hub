# Unity 캐시/설정을 D:로 이동하고 C:에는 junction 유지
# Unity Editor·Unity Hub 완전 종료 후 실행하세요.

$ErrorActionPreference = "Stop"
$DestRoot = "D:\_06.ProgramFiles\Unity"

$moves = @(
    @{ Source = "$env:LOCALAPPDATA\Unity"; Target = "$DestRoot\Local-Unity"; Link = "$env:LOCALAPPDATA\Unity" },
    @{ Source = "$env:APPDATA\Unity"; Target = "$DestRoot\Roaming-Unity"; Link = "$env:APPDATA\Unity" }
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

    New-Item -ItemType Directory -Path $DestRoot -Force | Out-Null
    if (Test-Path $Target) { throw "대상이 이미 있습니다: $Target" }

    Write-Host "이동: $Source -> $Target"
    robocopy $Source $Target /E /MOVE /R:1 /W:1 /NFL /NDL /NJH /NJS | Out-Null
    if ($LASTEXITCODE -ge 8) { throw "robocopy 실패 (code $LASTEXITCODE)" }

    if (Test-Path $Source) { Remove-Item $Source -Recurse -Force -EA SilentlyContinue }
    if (Test-Path $Link) { Remove-Item $Link -Force -EA SilentlyContinue }

    cmd /c "mklink /J `"$Link`" `"$Target`""
    if ($LASTEXITCODE -ne 0) { throw "junction 실패: $Link" }
    Write-Host "OK: $Link -> $Target"
}

Write-Host "=== Unity -> D: ==="
foreach ($m in $moves) { Move-AndLink $m.Source $m.Target $m.Link }
Write-Host "완료. Unity / Unity Hub를 다시 실행하세요."
