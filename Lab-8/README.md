# Practical 8 - Exceptions and Fault-Tolerant MiniBank

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A implements a guarded calculator that retries invalid input and a warehouse that reports out-of-stock and invalid-quantity errors while continuing through a batch. Part B adds a checked MiniBank exception hierarchy, validated deposits and withdrawals, exception-aware transfers, `finally` blocks, and an `AutoCloseable` session. The optional standalone try-with-resources exercise and supplementary daily-limit exception are not included; MiniBank demonstrates the required resource handling.

## Aim

Practice checked exceptions, custom exception data, `throw`/`throws`, error propagation, `try`/`catch`/`finally`, and try-with-resources.

## Source Files

- Part A programs: [`pra1`](./pra1)
- MiniBank model and annotations: [`pra2/model`](./pra2/model) and [`pra2/model/annotation`](./pra2/model/annotation)
- Bank exception hierarchy: [`pra2/exception`](./pra2/exception)
- MiniBank services and reflection validation: [`pra2/service`](./pra2/service) and [`pra2/util`](./pra2/util)

## Compile and Run

From `Lab-8\pra1`:

```powershell
javac -Xlint:all *.java
java GuardedCalculator
java WarehouseDemo
```

The calculator reads one line each for the first number, operator, and second number. For example, enter `bad`, `+`, `4` to test invalid-number handling, then `10`, `/`, `0` to test divide-by-zero handling, then `8`, `*`, `3` for a successful calculation.

From `Lab-8\pra2`:

```powershell
javac -Xlint:all -d out .\exception\*.java .\model\annotation\*.java .\model\*.java .\util\*.java .\service\*.java
java -cp out service.MiniBank
```

Enter `5` at the menu to exit. JDK 17 or newer is required.

## Expected Checks

- The calculator reports invalid numbers and division by zero separately, logs every attempt in `finally`, and stops after a successful result.
- The warehouse issues valid stock, reports the shortfall for insufficient stock, rejects non-positive quantities, and continues processing later requests.
- Withdrawing `5000` from an account holding `1000` prints a shortfall of `4000`; the balance remains `1000`.
- A non-positive withdrawal or deposit and a missing transfer destination produce friendly checked-exception messages.
- An insufficient transfer rethrows the original bank exception with its shortfall intact.
- A transfer attempt logs its `finally` action; the custom session closes when the try-with-resources block exits, including when a checked exception is thrown.
- Practical 7 field annotations and reflection validation remain active in MiniBank.

## Explanation

`DivideByZeroException`, `OutOfStockException`, and `InvalidQuantityException` are checked exceptions. The warehouse's `issue` method validates each request before mutating stock; its demo catches request failures independently so the batch continues.

The `exception` package defines `BankException` and its checked subclasses. `InsufficientFundsException` stores the computed shortfall. `Account.withdraw` and `Account.deposit` throw explicit failures instead of returning a failure-shaped result, and `Account.transfer` uses `try`/`catch`/`finally` while propagating the original `BankException`. In Practical 8, accounts preserve their previous savings, overdraft, and fixed-deposit withdrawal limits.

`BankSession` implements `AutoCloseable`; MiniBank uses it in try-with-resources and handles a checked exception from inside the block. The `finally` block still runs after the resource is closed.

## Word Submission

Prepare `25CS104_MeetTanti_Practical8.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots, and your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
