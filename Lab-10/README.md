# Practical 10 - Thread Pools, Producer-Consumer, and Deadlock

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A runs ten tasks on a fixed three-thread pool and coordinates a bounded number buffer with `wait()`/`notify()`. Part B adds a four-thread MiniBank transaction processor, a bounded array buffer for transaction tasks, and transfers that first demonstrate opposite-order deadlock and then complete using consistent account-number lock ordering. The optional standalone deadlock exercise is not included.

## Aim

Use `ExecutorService`, submit and await tasks, coordinate producer-consumer threads, demonstrate deadlock, and prevent it through consistent lock ordering.

## Source Files

- Part A programs: [`pra1`](./pra1)
- MiniBank model, annotations, and exceptions: [`pra2/model`](./pra2/model), [`pra2/model/annotation`](./pra2/model/annotation), and [`pra2/exception`](./pra2/exception)
- Transaction processing and coordination: [`pra2/service`](./pra2/service)

## Compile and Run

From `Lab-10\pra1`:

```powershell
javac -Xlint:all *.java
java ThreadPoolRunner
java ProducerConsumerDemo
```

From `Lab-10\pra2`:

```powershell
javac -Xlint:all -d out .\exception\*.java .\model\annotation\*.java .\model\*.java .\util\*.java .\service\*.java
java -cp out service.MiniBank
```

Enter `5` at the menu to exit. JDK 17 or newer is required.

## Expected Checks

- The Part A thread pool completes ten tasks using no more than three worker threads.
- The Part A consumer receives numbers `1` through `10` in order with none lost.
- MiniBank's fixed four-thread processor completes ten balanced deposit/withdrawal tasks and waits for completion.
- The transaction array buffer processes all six transactions in FIFO order using `wait()` and `notify()`.
- Opposite-order transfer threads are detected deadlocked; the test threads are daemon threads so the demonstration cannot keep the process alive.
- Account-number-ordered transfers finish and leave both account balances unchanged at `1000`.

## Explanation

`ThreadPoolRunner` uses a fixed pool of three workers and awaits its submitted tasks. The Part A bounded buffer and MiniBank's `TransactionBuffer` protect their arrays with synchronized blocks, wait in `while` loops for the appropriate condition, and notify the waiting producer or consumer after each state change.

`TransactionProcessor` owns a fixed pool of four threads. `submit` delegates to `execute`, while `stop` shuts down the pool and awaits termination. `TransactionTask` handles checked banking failures per transaction.

`TransferCoordinator` has an intentionally unsafe version that locks source then destination. Two daemon threads transfer in opposite directions and use a barrier to ensure both hold their first lock before attempting the second. The corrected method compares immutable account numbers and always locks the lower-numbered account first. The deadlock demo accounts are separate from the corrected demo accounts.

The optional standalone Part A deadlock exercise is for advanced learners and is not included; the required MiniBank deadlock demonstration is included.

## Word Submission

Prepare `25CS104_MeetTanti_Practical10.docx` with the program title and problem summary, complete Java source code, actual VS Code execution screenshots showing pool reuse, ordered producer-consumer output, and both deadlock outcomes, plus your own GitHub repository URL in the footer once that repository exists. Use Consolas or Courier New for source code. No screenshots or Word document have been fabricated or created here.

Capture real terminal output in VS Code and use Windows Snipping Tool (`Win`+`Shift`+`S`).
