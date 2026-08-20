# Ulster University — Academic Project Repository

**A structured, version-controlled academic workspace for Ulster University coursework, software development modules, and collaborative project materials.**

[Overview](#-overview) · [Structure](#-repository-structure) · [Modules](#-modules) · [Technologies](#-technologies) · [Setup](#-getting-started) · [Contributing](#-contributing)

---

## 📌 Overview

This repository serves as a centralised, version-controlled hub for all academic work undertaken at **Ulster University**. It covers multiple computing modules spanning software engineering, computer networking, data analytics, database management, Python programming, and web/client-side development.

The repository is structured to ensure clear separation between modules while allowing shared tools and references to remain accessible. It supports:

- ✅ Organised, module-specific storage of coursework, reports, scripts, and documentation
- ✅ Full version history of all project iterations via Git
- ✅ Collaborative workflows for group assignments and paired programming
- ✅ A reference base for future academic and professional projects

> **Institution:** Ulster University, London
> **Repository Owner:** [rukonuzzamantopu](https://github.com/rukonuzzamantopu)
> **Language Breakdown:** Python · R · TeX · Jupyter Notebook · HTML · T-SQL · CSS · JavaScript

---

## 🗂️ Repository Structure

```
Ulster-University/
│
├── 📁 CMP301 Computer Networking/
│   ├── Networking coursework, configuration files, and lab reports
│   └── PowerPoint slides (.pptx)
│
├── 📁 COM161 Coding Project (Python)/
│   └── komal/
│       ├── Python scripts (.py)
│       └── Jupyter Notebooks (.ipynb)
│
├── 📁 COM398 Systems Security/
│   └── Systems security coursework and assignment materials
│
├── 📁 COM410 Programming in Practice/
│   └── Programming in Practice coursework and projects
│
├── 📁 COM435 Software Product and Process Management/
│   ├── Reports and documentation (LaTeX / PDF)
│   ├── Process management artefacts (plans, diagrams, reviews)
│   └── Assignment submissions and coursework materials
│
├── 📁 Client-Side Development/
│   ├── HTML pages
│   ├── CSS stylesheets
│   ├── JavaScript files
│   └── In-progress front-end coursework projects
│
├── 📁 Data Analytics (CMP330)/
│   ├── R scripts (.r)
│   ├── R Markdown files (.rmd)
│   └── Rendered HTML reports (.html)
│
├── 📁 INTRODUCTION TO DATABASES/
│   ├── SQL and T-SQL scripts (queries, stored procedures)
│   ├── Database schema designs and ER diagrams
│   └── Assignment-related database files
│
├── 📦 coffe-shop-webpage/          ← Git Submodule
│   └── (links to: rukonuzzamantopu/coffe-shop-webpage)
│       A standalone responsive coffee shop website project
│
├── .gitmodules                     ← Submodule configuration
└── .gitignore
```

> **Note:** The `coffe-shop-webpage` directory is a **Git submodule** — a separate linked repository. Use `--recurse-submodules` when cloning to include it (see [Getting Started](#-getting-started)).

---

## 📘 Modules

### CMP301 — Computer Networking

| Item           | Detail                                                                                     |
| -------------- | ------------------------------------------------------------------------------------------- |
| **Module Code** | CMP301                                                                                      |
| **Key Topics**  | Network fundamentals, protocols (TCP/IP), subnetting, routing & switching, network security |
| **Artefacts**   | Lab reports, configuration files, coursework write-ups, PowerPoint slides (.pptx)           |

### Data Analytics (CMP330)

| Item           | Detail                                                                                  |
| -------------- | ---------------------------------------------------------------------------------------- |
| **Module Code** | CMP330                                                                                   |
| **Key Topics**  | Data cleaning & preparation, exploratory analysis, statistical methods, data visualisation |
| **Tools Used**  | R, R Markdown                                                                            |
| **Artefacts**   | R scripts (`.r`), R Markdown files (`.rmd`), rendered HTML reports (`.html`)             |

### Client-Side Development

| Item           | Detail                                                                                 |
| -------------- | ---------------------------------------------------------------------------------------- |
| **Focus Area** | Frontend web development (currently in progress)                                        |
| **Key Topics** | HTML5 structure, CSS3 styling and layouts, JavaScript interactivity, responsive design  |
| **Tools Used** | HTML, CSS, JavaScript                                                                    |
| **Artefacts**  | Web pages, UI components, the coffee shop project (submodule)                           |

### COM435 — Software Product and Process Management

| Item            | Detail                                                                                                                                                                 |
| --------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Module Code** | COM435                                                                                                                                                                  |
| **Key Topics**  | Software Development Life Cycle (SDLC), Agile & Scrum methodologies, project planning, risk management, quality assurance, process documentation, team collaboration |
| **Artefacts**   | Reports, plans, process models, reviews                                                                                                                                |
| **Format**      | LaTeX (TeX) for academic reports; PDF outputs                                                                                                                          |

### COM398 — Systems Security

| Item           | Detail                                                            |
| -------------- | -------------------------------------------------------------------- |
| **Module Code** | COM398                                                               |
| **Key Topics**  | Security fundamentals, threat analysis, secure systems design       |
| **Artefacts**   | Coursework reports and assignment submissions                       |

### COM410 — Programming in Practice

| Item           | Detail                                                          |
| -------------- | ------------------------------------------------------------------ |
| **Module Code** | COM410                                                             |
| **Key Topics**  | Applied programming projects, coding standards, software practice |
| **Artefacts**   | Source code, coursework write-ups                                 |

### Introduction to Databases

| Item           | Detail                                                                                                                       |
| -------------- | ------------------------------------------------------------------------------------------------------------------------------ |
| **Focus Area** | Relational Database Design & SQL Development                                                                                  |
| **Key Topics** | Entity-Relationship (ER) modelling, schema normalisation, T-SQL queries, stored procedures, data manipulation and retrieval    |
| **Tools Used** | Microsoft SQL Server / T-SQL                                                                                                   |
| **Artefacts**  | SQL scripts, schema designs, ER diagrams, query assignments                                                                    |

### COM161 — Coding Project (Python)

| Item           | Detail                                                                                        |
| -------------- | --------------------------------------------------------------------------------------------- |
| **Focus Area** | Python scripting and data exploration                                                         |
| **Key Topics** | Core Python programming, data structures, file handling, data analysis with Jupyter Notebooks |
| **Tools Used** | Python 3.x, Jupyter Notebook                                                                  |
| **Artefacts**  | `.py` scripts, `.ipynb` notebooks (in `komal/`)                                               |

---

## 🛠️ Technologies

| Category             | Technology / Tool            | Version / Notes            |
| --------------------- | ----------------------------- | --------------------------- |
| Programming Language  | Python                        | 3.x                         |
| Data Exploration      | Jupyter Notebook               | via Anaconda or pip         |
| Statistical Computing | R / R Markdown                 | RStudio recommended         |
| Database               | Microsoft SQL Server / T-SQL   | SSMS recommended            |
| Networking             | Network protocols & tools      | CMP301 coursework           |
| Presentations           | Microsoft PowerPoint           | .pptx slide decks           |
| Web — Markup           | HTML5                          | Semantic structure          |
| Web — Styling          | CSS3                           | Responsive design           |
| Web — Scripting        | JavaScript                     | Vanilla JS                  |
| Academic Writing       | LaTeX (TeX)                    | Overleaf or local TeX Live  |
| Version Control        | Git                            | 2.x                          |
| Hosting                 | GitHub                         | github.com                   |

---

## 🚀 Getting Started

### Prerequisites

Ensure the following are installed on your machine before cloning:

| Tool                  | Download                                                        |
| --------------------- | ------------------------------------------------------------------ |
| Git                    | <https://git-scm.com/downloads>                                    |
| Python 3.x             | <https://www.python.org/downloads/>                                |
| Jupyter Notebook       | `pip install notebook` or [Anaconda](https://www.anaconda.com/)    |
| SQL Server / SSMS      | <https://aka.ms/ssmsfullsetup>                                     |
| R / RStudio             | <https://posit.co/download/rstudio-desktop/>                       |
| VS Code (recommended)  | <https://code.visualstudio.com/>                                   |
| TeX Live (optional)    | <https://www.tug.org/texlive/>                                     |

### 1. Clone the Repository

To clone the repository **including the submodule** (`coffe-shop-webpage`):

```
git clone --recurse-submodules https://github.com/rukonuzzamantopu/Ulster-University.git
```

If you have already cloned without the flag, initialise the submodule manually:

```
cd Ulster-University
git submodule update --init --recursive
```

### 2. Explore a Module

Navigate into any module folder directly:

```
# Example — Computer Networking module
cd "CMP301 Computer Networking"

# Example — Data Analytics module
cd "Data Analytics (CMP330)"

# Example — Client-Side Development module
cd "Client-Side Development"
```

### 3. Run Python Scripts

```
cd "COM161 Coding Project (Python)/komal"
python your_script_name.py
```

### 4. Launch Jupyter Notebooks

```
# Install if not already available
pip install notebook

# Start the Jupyter server
jupyter notebook
```

Then open the `.ipynb` file of your choice from the browser interface.

### 5. Run R Scripts / R Markdown

Open **RStudio**, then:

```
# Open the Data Analytics folder
cd "Data Analytics (CMP330)"
```

- Open any `.r` file and run it directly in RStudio.
- Open any `.rmd` file and click **Knit** to render it to HTML (the corresponding `.html` output is also included in the folder for quick viewing without RStudio).

### 6. Run SQL Scripts

Open **SQL Server Management Studio (SSMS)** or **Azure Data Studio**, connect to your local SQL Server instance, and open any `.sql` file from the `INTRODUCTION TO DATABASES/` folder to execute queries.

### 7. View the Web / Client-Side Projects

Open any `.html` file from the `Client-Side Development/` or `coffe-shop-webpage/` folder directly in your browser, or use the **Live Server** extension in VS Code for hot-reload development.

---

## 📐 Academic & Coding Standards

| Domain    | Standard                                                                             |
| --------- | --------------------------------------------------------------------------------------- |
| Python     | [PEP 8](https://peps.python.org/pep-0008/) — consistent naming, spacing, docstrings    |
| SQL        | Capitalised keywords; meaningful table/column names; commented scripts                 |
| HTML/CSS   | Semantic HTML5; BEM-style class naming where applicable                                |
| LaTeX      | Structured documents with sections, references, and bibliography                        |
| Git        | Descriptive commit messages; feature branches for new work                             |

---

## 🤝 Contributing

Contributions are welcome, especially from fellow Ulster University students.

```
# 1. Fork this repository on GitHub

# 2. Clone your fork
git clone https://github.com/YOUR_USERNAME/Ulster-University.git

# 3. Create a new feature branch
git checkout -b feature/your-feature-name

# 4. Make your changes and commit
git add .
git commit -m "feat: brief description of your change"

# 5. Push to your fork
git push origin feature/your-feature-name

# 6. Open a Pull Request on GitHub
```

### Commit Message Convention

This repository follows a simplified [Conventional Commits](https://www.conventionalcommits.org/) standard:

| Prefix      | Use For                                            |
| ----------- | --------------------------------------------------- |
| `feat:`     | New file, script, or module content added           |
| `fix:`      | Bug fixes or corrections                             |
| `docs:`     | README or documentation updates                      |
| `refactor:` | Code restructuring without changing functionality    |
| `style:`    | Formatting, spacing, naming changes only              |
| `chore:`    | Maintenance tasks, dependency updates                 |

---

## 📋 Changelog

| Version | Date    | Description                                                                          |
| ------- | ------- | -------------------------------------------------------------------------------------- |
| v1.0    | Initial | Repository created with base structure                                                |
| v1.1    | Update  | Added COM398 Systems Security and COM410 Programming in Practice modules              |
| v1.2    | Update  | Added CMP301 Computer Networking and Data Analytics (CMP330) modules                  |
| v1.3    | Ongoing | Active work on Client-Side Development module                                        |
| v1.4    | Update  | Data Analytics (CMP330) uses R/R Markdown/HTML; added PowerPoint slides to CMP301    |

---

## ❓ FAQ

**Q: Can I reuse the code here in my own academic submission?**
A: No. Reusing submitted academic work may violate Ulster University's Academic Integrity Policy. You may use this repo as a **reference or learning resource** only.

**Q: Why does cloning fail for the coffee shop submodule?**
A: Run `git clone --recurse-submodules ...` instead of a plain `git clone`. See the [Getting Started](#-getting-started) section.

**Q: Which Python version is required?**
A: Python **3.6 or higher** is recommended. Python 2 is not supported.

**Q: How do I run `.ipynb` notebooks without Jupyter?**
A: You can view them directly on GitHub, or use [Google Colab](https://colab.research.google.com/) by uploading the file — no local install needed.

---

## 📄 License & Academic Integrity

> ⚠️ **Important Notice**

This repository is intended for **academic, educational, and portfolio purposes only**.

- All code and documents are the original work of the repository owner and collaborators.
- Do **not** copy, submit, or reuse any work here as your own for academic submissions — doing so may constitute **academic misconduct** under [Ulster University's Academic Integrity Policy](https://www.ulster.ac.uk/student/wellbeing/academic-integrity).
- You are welcome to **reference, study, or fork** this repository for personal learning.

---

## 📬 Contact

| Platform          | Link                                                                          |
| ------------------ | -------------------------------------------------------------------------------- |
| GitHub Profile      | [@rukonuzzamantopu](https://github.com/rukonuzzamantopu)                        |
| Institution         | [Ulster University](https://www.ulster.ac.uk)                                    |
| Repository Issues   | [Open an Issue](https://github.com/rukonuzzamantopu/Ulster-University/issues)   |

---

*Organised · Version-Controlled · Collaborative*
