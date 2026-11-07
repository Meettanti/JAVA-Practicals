# Practical 11 - Object Persistence, Logs, and NIO

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A serializes and reloads a student record, analyzes application-log levels, and includes the optional NIO directory report. Part B makes MiniBank accounts serializable, saves and reloads the account list, appends successful account and transaction events to a log, and generates a strict NIO end-of-day summary.

## Source Files

- Part A: [`pra1`](./pra1) — `StudentRecord`, `ObjectPersistenceDemo`, `LogAnalyzer`, and optional `DirectoryReport`
- MiniBank models and exceptions: [`pra2/model`](./pra2/model), [`pra2/model/annotation`](./pra2/model/annotation), and [`pra2/exception`](./pra2/exception)
- Persistence, application menu, logging, and report services: [`pra2/service`](./pra2/service)

## Compile and Run

From `Lab-11\pra1`:

```powershell
javac -Xlint:all -d out *.java
java -cp out ObjectPersistenceDemo data\student.ser
java -cp out LogAnalyzer sample-application.txt
java -cp out DirectoryReport .
```

From `Lab-11\pra2`:

```powershell
javac -Xlint:all -d out .\exception\*.java .\model\annotation\*.java .\model\*.java .\util\*.java .\service\*.java
java -cp out service.MiniBank
```

The MiniBank menu keeps option `5` as save-and-exit. Options `1` through `4` open accounts, deposit, withdraw, and transfer; option `6` saves, `7` reloads, and `8` generates the report. JDK 17 or newer is required.

## Expected Checks

- The Part A serialization demo prints the same record after saving and loading it.
- The log analyzer reports `INFO: 2`, `WARN: 1`, and `ERROR: 2` for the supplied sample.
- The optional directory report counts regular files and their total size without following symbolic links.
- MiniBank saves supported account types to a temporary file before replacing the snapshot, reloads through a class filter, and advances account IDs past restored IDs.
- The transaction log is opened with `CREATE` and `APPEND`; successful account opening, deposits, withdrawals, and both sides of transfers are recorded.
- The end-of-day report totals transaction types and amounts, and reports malformed log entries with their line number.

Runtime data is written beneath each part's `data` directory and is excluded by [`.gitignore`](./.gitignore). Account snapshots and reports are replaced on save/generation; the transaction log is append-only.

## Word Submission

Prepare `25CS104_MeetTanti_Practical11.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots showing object round-trip, log counts, account save/load, append-only transactions, and the end-of-day report, plus your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
