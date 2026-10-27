# PAWTH setup

See [ReadMe.md](ReadMe.md) for repository setup, IntelliJ instructions, bundled libraries, demo accounts, and the remaining-work report.

- Run with the repository root as the working directory.
- Configure your installed JDK 17 or newer in IntelliJ.
- Copy config/database.properties.example to config/database.properties and enter your own local MySQL credentials.
- Start MySQL before logging in.
- The database initialization SQL drops and recreates pawth_db.
- The actual database configuration and runtime files are excluded from Git.
- Start-MySQL.cmd and Stop-MySQL.cmd apply only to an existing project-local MySQL 8.4.13 runtime, not a fresh clone without that runtime.
- scripts/Build.ps1 compiles all application sources without Maven.
- The UI currently displays only Buddy. Enabled write actions still update the configured database.
