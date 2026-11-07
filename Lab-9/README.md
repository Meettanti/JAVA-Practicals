# Practical 9 - Threads, Race Conditions, and Synchronization

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A compares unsynchronized and synchronized counter increments and seat booking. Part B runs concurrent MiniBank deposit workers, prints thread names, demonstrates the race with a clearly named unsynchronized test method, and then uses synchronized account operations to reach the exact expected balance. The optional parallel-summation exercise is not included.

## Aim

Create and join threads, implement `Runnable`, observe thread lifecycle messages, reproduce race conditions, and protect shared account state with `synchronized`.

## Source Files

- Part A programs: [`pra1`](./pra1)
- MiniBank model and annotations: [`pra2/model`](./pra2/model) and [`pra2/model/annotation`](./pra2/model/annotation)
- Bank exceptions: [`pra2/exception`](./pra2/exception)
- MiniBank workers and services: [`pra2/service`](./pra2/service)

## Compile and Run

From `Lab-9\pra1`:

```powershell
javac -Xlint:all *.java
java CounterRaceDemo
java SeatBookingRaceDemo
```

From `Lab-9\pra2`:

```powershell
javac -Xlint:all -d out .\exception\*.java .\model\annotation\*.java .\model\*.java .\util\*.java .\service\*.java
java -cp out service.MiniBank
```

Enter `5` at the menu to exit. JDK 17 or newer is required.

## Expected Checks

- The counter race prints a total below `10000`; the synchronized counter prints exactly `10000`.
- Ten simultaneous seat requests oversell the five-seat unsynchronized booking, while the synchronized version accepts exactly five.
- Ten bank workers each deposit `1` one thousand times. The deliberately unsynchronized demonstration produces a balance below `10000`; the regular synchronized `deposit` produces exactly `10000`.
- Worker thread names show each worker starting and finishing.
- The previous MiniBank exception and annotation demonstrations continue to run.

## Explanation

The counter and account race demonstrations read the shared value, yield, and then write the incremented value without protecting the read-modify-write sequence. The seat demo coordinates customers at the availability check so all can observe the same five seats before decrementing. These harnesses make the lost updates and overselling visible.

`AccountWorker` implements `Runnable` and calls the normal synchronized `Account.deposit` method. `UnsynchronizedAccountWorker` uses `depositUnsynchronizedForDemo` solely to reproduce the earlier race condition. Production `deposit`, `withdraw`, `getBalance`, and account withdrawal-rule reads are synchronized. The demo starts ten workers, joins each one, and compares both outcomes.

The optional parallel-summation exercise is for advanced learners and is not included.

## Word Submission

Prepare `25CS104_MeetTanti_Practical9.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots showing both incorrect and corrected totals, and your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
