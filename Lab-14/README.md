# Practical 14 - Layered Application and Integrated MiniBank

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A assembles a packaged task application with model, repository, service, and file-store layers, JUnit tests, persistence, and an executable JAR. Part B packages the MiniBank project with its validation, exceptions, concurrency, persistent state, reflective `@Id` repository, JUnit coverage, and runnable JAR.

## Part A - Layered Task Application

Source and tests are under [`pra1/src`](./pra1/src). The project uses JUnit Jupiter and stores task data in `data\tasks.ser`.

From `Lab-14\pra1`:

```powershell
mvn test package
java -jar .\target\layered-task-practical14-1.0.0.jar
```

The JAR prints the task list and saves it. Subsequent runs reload the same task IDs and statuses. JUnit tests cover service validation/completion and the serialization round-trip.

## Part B - MiniBank Integration

The MiniBank project preserves the earlier model, validation, exception, transaction, persistence, and generic repository layers. JUnit tests cover account validation, repository CRUD/varargs/concurrent creation, serialized account types, and transaction processor completion.

From `Lab-14\pra2`:

```powershell
mvn test package
java -jar .\target\minibank-practical14-1.0.0.jar
```

Enter `5` at the MiniBank menu to save and exit. The app writes persistent runtime data under `data\minibank`; it is excluded from source control. Maven and JDK 17 or newer are required. JUnit Jupiter is declared in each `pom.xml`.

## Validation Results

- Part A: 3 JUnit tests passed; the executable JAR ran twice and restored the same persisted tasks.
- Part B: 7 JUnit tests passed; the runnable JAR reached the MiniBank menu and saved state on exit.
- Both projects compiled with Java 17 release settings.

## Word Submission

Prepare `25CS104_MeetTanti_Practical14.docx` with the program title and problem summary, complete Java source code, actual VS Code screenshots showing Part A's JUnit tests and persistence round-trip, Part B's JUnit tests and MiniBank JAR execution, plus your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
