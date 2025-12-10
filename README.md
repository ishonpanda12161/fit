# FIT — A Lightweight Version Control System (with AI-Powered Commands)

FIT is a minimal, fast, and fully offline version control system inspired by Git.  
It supports commits, branches, merges, logs, and even has an AI assistant that converts natural language into real FIT commands.

Perfect for learning how version control works internally — or for experimenting with your own DVCS.

---

## Features

### Core Version Control
- Track files with `fit add`
- Commit changes with `fit commit`
- View staged/modified files with `fit status`
- Browse history with `fit log` and `fit log --graph`
- Checkout any commit
- Create, switch, delete, and rename branches
- Merge branches with a simple 3-way merge algorithm
- Fast-forward merging (`fit promote`)

### 🤖 AI-Assisted Natural Language Commands
## MAY NOT WORK FOR YOU DUE TO API LIMITATIONS
FIT includes a built-in AI layer (Groq API) that translates plain English into real FIT commands:

```bash
fit ai "create a new branch for testing"
→ automatically runs: fit branch testing
fit ai "show me history"
→ fit log --graph

```


📦 Installation (Windows)
1. Download fit.zip from the Releases page

Unzip it — you will find:

fit.jar
install-fit.bat
uninstall-fit.bat

2. Install FIT globally

Right-click install-fit.bat → Run as administrator

This installs FIT to:

C:\Program Files\fit\


and registers the fit command system-wide.

3. Test installation
fit aitest
