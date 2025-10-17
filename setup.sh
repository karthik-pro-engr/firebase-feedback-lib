#!/usr/bin/env bash
# setup.sh - Bootstrap script: create package.json (as requested), install commitlint/husky/lint-staged/ktlint,
# and create .husky/pre-commit + .husky/commit-msg hooks.
# Usage: ./setup.sh
set -euo pipefail

echo "📦 Bootstrap: create package.json -> install devDependencies -> configure husky/commitlint/lint-staged"

# Detect OS
OS="$(uname -s 2>/dev/null || echo Unknown)"
case "$OS" in
  Linux*)   platform="Linux" ;;
  Darwin*)  platform="macOS" ;;
  CYGWIN*|MINGW*|MSYS*) platform="Windows" ;;
  *)        platform="Unknown" ;;
esac
echo "🔍 Platform detected: $platform"

# 0) Ensure node/npm are available
if ! command -v npm >/dev/null 2>&1; then
  echo "❌ npm not found. Please install Node.js + npm and re-run."
  exit 1
fi

# 1) Ensure git is available (husky needs a git repo)
if ! command -v git >/dev/null 2>&1; then
  echo "❌ git not found. Please install Git and re-run."
  exit 1
fi

# 2) Ensure we're in project root (best-effort)
REPO_ROOT="$(pwd)"
echo "📁 Using project root: $REPO_ROOT"

# 3) Backup existing package.json if present, then write the exact package.json the user supplied
PKG="package.json"
if [ -f "$PKG" ]; then
  timestamp=$(date +%s)
  backup="${PKG}.bak.${timestamp}"
  echo "⚠️ Existing $PKG detected — creating backup: $backup"
  cp "$PKG" "$backup"
fi

cat > "$PKG" <<'JSON'
{
  "name": "firebasefeedbacklib",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "prepare": "husky install",
    "lint": "ktlint --android '**/*.kt'",
    "lint:fix": "ktlint --format \"**/*.kt\"",
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "keywords": [],
  "author": "",
  "license": "ISC",
  "devDependencies": {
    "@commitlint/cli": "^20.1.0",
    "@commitlint/config-conventional": "^20.0.0",
    "husky": "^9.1.7",
    "lint-staged": "^16.2.4",
    "ktlint": "^0.0.5"
  }
}
JSON

echo "✅ Written $PKG"

# 4) npm install (devDependencies)
echo "📥 Installing devDependencies from package.json (this may take a moment)..."
npm install --no-audit --no-fund

# 5) Create commitlint config if missing
if [ ! -f commitlint.config.cjs ] && [ ! -f commitlint.config.js ]; then
  echo "🧾 Creating commitlint.config.cjs..."
  cat > commitlint.config.cjs <<'CJS'
module.exports = {
  extends: ['@commitlint/config-conventional'],
};
CJS
else
  echo "ℹ️ commitlint config already exists — skipping creation."
fi

# 6) Prepare lint-staged config: prefer .lintstagedrc.json if not present and not present in package.json
lintstaged_in_pkg=false
if node -e "try{ process.exit(Boolean(require('./package.json').['lint-staged'])?0:2) }catch(e){ process.exit(3) }" 2>/dev/null; then
  lintstaged_in_pkg=true
fi

if [ ! -f .lintstagedrc.json ] && ! $lintstaged_in_pkg; then
  echo "📋 Creating .lintstagedrc.json..."
  cat > .lintstagedrc.json <<'JSON'
{
  "*.{js,jsx,ts,tsx,json,css,scss,md}": [
    "prettier --write",
    "git add"
  ]
}
JSON
else
  echo "ℹ️ lint-staged config already present (package.json or .lintstagedrc.json)."
fi

# 7) Ensure git repo is initialized
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "➡️ Not a git repo yet — initializing git repository..."
  git init
  git add -A
  git commit -m "chore: initial commit (bootstrap)" || true
fi

# 8) Husky install + add hooks (idempotent)
echo "🔧 Installing Husky hooks..."
npx husky install

# Add commit-msg hook (commitlint)
if [ ! -f .husky/commit-msg ]; then
  echo "➕ Adding .husky/commit-msg hook..."
  npx husky add .husky/commit-msg 'npx --no-install @commitlint/cli --edit "$1"'
else
  echo "ℹ️ .husky/commit-msg already exists — skipping."
fi

# Add pre-commit hook (lint-staged)
if [ ! -f .husky/pre-commit ]; then
  echo "➕ Adding .husky/pre-commit hook..."
  npx husky add .husky/pre-commit 'npx --no-install lint-staged'
else
  echo "ℹ️ .husky/pre-commit already exists — skipping."
fi

# 9) Ensure hooks are executable
if [ -d ".husky" ]; then
  case "$platform" in
    Linux|macOS)
      chmod +x .husky/* || true
      ;;
    Windows)
      # Git Bash/WSL supports chmod; fallback otherwise
      chmod +x .husky/* || true
      echo "⚠️ On Windows: if hooks don't run, use Git Bash (Git for Windows) or WSL and ensure core.autocrlf does not break shebangs."
      ;;
    *)
      chmod +x .husky/* || true
      ;;
  esac
fi

# 10) Post-checks and helpful messages
echo ""
echo "✅ Bootstrap finished!"
echo ""
echo "What was created/ensured:"
echo "  • $PKG (backed up previous if present)"
echo "  • commitlint.config.cjs (if absent)"
echo "  • .lintstagedrc.json (if absent and not in package.json)"
echo "  • husky installed and .husky/{pre-commit,commit-msg} hooks (if previously missing)"
echo ""
echo "Quick test suggestions:"
echo "  1) Try a commit that violates conventional commits to see commitlint fail:"
echo "       git add -A"
echo "       git commit -m \"bad commit message\" || true"
echo ""
echo "  2) Try a normal conventional commit:"
echo "       git add -A"
echo "       git commit -m \"chore: verify setup\""
echo ""
echo "If you want to add prettier or change lint-staged rules to run ktlint on .kt files,"
echo "I can provide the exact lint-staged entry (e.g., run ktlint --format and git add) — tell me and I'll paste it."
echo ""
echo "Good luck with clean commits and the 1 Crore job prep — paste any errors here and I'll help debug! 🚀"
