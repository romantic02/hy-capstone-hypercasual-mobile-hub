$ErrorActionPreference = "Continue"
$LogPath = Join-Path $PSScriptRoot "push-output.log"
$Root = Split-Path $PSScriptRoot -Parent

function Log($msg) {
    $line = "[$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')] $msg"
    Add-Content -Path $LogPath -Value $line -Encoding utf8
    Write-Host $line
}

"" | Set-Content -Path $LogPath -Encoding utf8
Set-Location $Root
Log "PWD: $(Get-Location)"

if (-not (Test-Path .git)) {
    Log "=== git init ==="
    git init 2>&1 | ForEach-Object { Log $_ }
    git branch -M main 2>&1 | ForEach-Object { Log $_ }
}

Log "=== git add ==="
git add . 2>&1 | ForEach-Object { Log $_ }
Log "=== git status --short ==="
git status --short 2>&1 | ForEach-Object { Log $_ }

$count = 0
try { $count = [int](git rev-list --count HEAD 2>$null) } catch {}
Log "Commit count: $count"
if ($count -eq 0) {
    Log "=== git commit ==="
    git commit -m "Initial commit: HY capstone hypercasual mobile game hub" 2>&1 | ForEach-Object { Log $_ }
}

Log "=== gh auth status ==="
gh auth status 2>&1 | ForEach-Object { Log $_ }
if ($LASTEXITCODE -ne 0) { Log "ERROR: gh not authenticated"; exit 1 }

Log "=== gh repo create ==="
gh repo create hy-capstone-hypercasual-mobile-hub --public --source=. --remote=origin --description "HY capstone hypercasual mobile game hub" --push 2>&1 | ForEach-Object { Log $_ }
if ($LASTEXITCODE -ne 0) {
    Log "=== fallback push ==="
    $user = gh api user -q .login 2>&1
    Log "User: $user"
    git remote remove origin 2>$null
    git remote add origin "https://github.com/$user/hy-capstone-hypercasual-mobile-hub.git" 2>&1 | ForEach-Object { Log $_ }
    git push -u origin main 2>&1 | ForEach-Object { Log $_ }
}

Log "=== REPO URL ==="
$url = gh repo view --json url -q .url 2>&1
Log "URL: $url"
Log "=== GIT LOG ==="
git log -1 --oneline 2>&1 | ForEach-Object { Log $_ }
Log "DONE exit=$LASTEXITCODE"
