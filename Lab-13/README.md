# Practical 13 - Generics and Reflection-Based Repository

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A demonstrates a typed generic box, bounded numeric statistics, and the optional generic pair. Part B uses `@Id` reflection in a `GenericRepository<T extends Serializable>` to provide CRUD operations and varargs lookup, then uses that repository for MiniBank account persistence and transfers.

## Source Files

- Part A: [`pra1`](./pra1) — `GenericBox`, `BoundedStatistics`, optional `Pair`, and `GenericsDemo`
- MiniBank models and annotations: [`pra2/model`](./pra2/model), [`pra2/model/annotation`](./pra2/model/annotation), and [`pra2/exception`](./pra2/exception)
- Reflection repository and demo: [`GenericRepository.java`](./pra2/service/GenericRepository.java) and [`GenericRepositoryDemo.java`](./pra2/service/GenericRepositoryDemo.java)
- MiniBank integration: [`BankApplication.java`](./pra2/service/BankApplication.java)

## Compile and Run

From `Lab-13\pra1`:

```powershell
javac -Xlint:all -d out *.java
java -cp out GenericsDemo
```

From `Lab-13\pra2`:

```powershell
javac -Xlint:all -d out .\exception\*.java .\model\annotation\*.java .\model\*.java .\util\*.java .\service\*.java
java -cp out service.GenericRepositoryDemo
java -cp out service.MiniBank
```

The MiniBank menu keeps option `5` as save-and-exit. Option `9` displays sorted transaction history; transfer uses the repository's varargs ID lookup. JDK 17 or newer is required.

## Expected Checks

- The generic box stores and returns values of its declared type; the optional pair stores two independently typed values.
- `BoundedStatistics<T extends Number & Comparable<T>>` returns count, minimum, maximum, mean, and median, and rejects empty or non-finite input.
- Repository initialization discovers the inherited `@Id` field on `Account`.
- `GenericRepository<T extends Serializable>` supports create, bulk create/replace, read, update, delete, find-all, and varargs ID lookup while rejecting missing or duplicate IDs.
- `GenericRepositoryDemo` exercises CRUD and confirms varargs lookup preserves the requested order.
- MiniBank uses the repository for account state, updates, and transfer endpoint lookup.

Runtime data is written beneath `pra2\data` and excluded by [`.gitignore`](./.gitignore).

## Word Submission

Prepare `25CS104_MeetTanti_Practical13.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots showing generic values/statistics, repository CRUD, varargs lookup, and MiniBank integration, plus your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
