# Practical 1 - MiniBank Menu Shell and Practice Programs

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Complete the first two Part A console exercises: accept the listed coin values until a snack costing 15 is paid for and return change; then record vehicle numbers and types until `done`, calculate tolls and report the most frequent type. For Part B, create a MiniBank header and numbered menu using a record, enum, `Scanner`, a loop and switch expressions; show placeholders for banking actions and exit when requested.

The official source is the practical-list PDF supplied at `C:\Users\meett\Downloads\2026-27-ODD-CEUE203-OOPJ-PracticalList.pdf`. The optional advanced RPSLS exercise is marked for home and was not included in this implementation.

## Aim

Practice Java records, enums, switch expressions, console input and menu-driven loops.

## Concepts Used

- `enum` and `record`
- Switch expressions
- `Scanner` input and loops
- Input validation for coin, vehicle type and menu choice

## Source Files

- [`VendingMachine.java`](./pra1/VendingMachine.java)
- [`TollBooth.java`](./pra1/TollBooth.java)
- [`MiniBank.java`](./pra2/MiniBank.java)

## Compile and Run

Run each command from the folder containing that source file. JDK 17 or newer is required.

```powershell
cd Lab-1\pra1
javac VendingMachine.java
java VendingMachine
```

In another run, compile and execute the toll booth:

```powershell
javac TollBooth.java
java TollBooth
```

Run the MiniBank menu from its folder:

```powershell
cd ..\pra2
javac MiniBank.java
java MiniBank
```

## Sample Checks

### Vending machine

Enter `TEN`, then `FIVE`:

```text
Total paid: 10
Total paid: 15
Paid. Change: 0
```

Enter `TEN`, then `TEN` in another run; the final line is `Paid. Change: 5`.

### Toll booth

Enter two distinct vehicle numbers with type `car`, one with type `bike`, then enter `done` at the vehicle-number prompt:

```text
Total toll: 120
Most frequent: car
```

If counts tie, the first type in the order bike, car, truck is reported.

### MiniBank

Enter `2`, then `5`:

```text
Deposit - to be implemented in a later lab.
Goodbye!
```

Menu choices 1 through 4 show their corresponding placeholders. Invalid choices are rejected and the menu is displayed again.

## Explanation

The vending machine converts the entered enum constant to a rupee value with a switch expression and stops accepting coins once the total reaches 15. The toll booth represents each vehicle with a record, chooses its toll using a switch expression, and maintains a counter for each type. MiniBank maps the numeric choice to a `MenuOption` in a switch expression, then displays the matching placeholder and repeats until `EXIT`.

## Word Submission

Prepare `25CS104_MeetTanti_Practical1.docx` with the program title and problem summary, complete source code, and an actual VS Code execution screenshot. Use Consolas or Courier New for code and include your own GitHub repository URL in the footer once that repository exists. No screenshot or Word document has been fabricated or created here.

To capture actual output, run each program in the VS Code terminal with the sample inputs above, then use Windows Snipping Tool (`Win`+`Shift`+`S`) to capture the terminal output.
