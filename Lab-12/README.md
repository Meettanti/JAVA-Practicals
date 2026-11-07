# Practical 12 - Collections, Sorting, and Concurrent Maps

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A counts words and unique visitors using collections and includes the optional score leaderboard. Part B adds a `ConcurrentHashMap` account store, presents accounts in sorted order, and loads and displays transaction history using a thread-safe collection and comparator.

## Source Files

- Part A: [`pra1`](./pra1) — `WordFrequencyAnalyzer`, `UniqueVisitors`, and optional `Leaderboard`
- MiniBank models and exceptions: [`pra2/model`](./pra2/model), [`pra2/model/annotation`](./pra2/model/annotation), and [`pra2/exception`](./pra2/exception)
- Collection-based account and transaction services: [`pra2/service`](./pra2/service)

## Compile and Run

From `Lab-12\pra1`:

```powershell
javac -Xlint:all -d out *.java
java -cp out WordFrequencyAnalyzer sample-text.txt
java -cp out UniqueVisitors sample-visitors.txt
java -cp out Leaderboard sample-scores.txt
```

From `Lab-12\pra2`:

```powershell
javac -Xlint:all -d out .\exception\*.java .\model\annotation\*.java .\model\*.java .\util\*.java .\service\*.java
java -cp out service.MiniBank
```

The MiniBank menu keeps option `5` as save-and-exit. Option `9` displays chronological transaction history; options `1` through `8` retain the account operations and persistence/report actions from Practical 11. JDK 17 or newer is required.

## Expected Checks

- The supplied text ranks `collections` and `data` at three occurrences each, then `java` and `organize` at two.
- The visitor sample contains four unique IDs and two duplicate visits.
- The optional leaderboard combines repeated player scores and sorts by descending score, then name.
- `AccountStore` uses `ConcurrentHashMap` for account lookup and sorted snapshots for display and persistence.
- `TransactionHistory` loads the append-only log into a `CopyOnWriteArrayList` and sorts records chronologically with a comparator, using account number and type as tie-breakers.
- The MiniBank transaction report and account state continue to work with the collection-backed services.

Runtime data is written beneath `pra2\data` and excluded by [`.gitignore`](./.gitignore).

## Word Submission

Prepare `25CS104_MeetTanti_Practical12.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots showing word counts, unique visitor totals, leaderboard ranking, sorted accounts, and sorted transaction history, plus your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
