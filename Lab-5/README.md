# Practical 5 - Inheritance and Runtime Polymorphism

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A uses an abstract `Shape` and subclasses to compute areas in one polymorphic loop, and an abstract `Employee` with full-time, part-time and intern salary implementations. Part B makes `Account` abstract and adds savings, current and fixed-deposit account types with different interest rates and withdrawal rules. The optional advanced media-fee exercise is marked for home and was not included.

The official source is the practical-list PDF supplied at `C:\Users\meett\Downloads\2026-27-ODD-CEUE203-OOPJ-PracticalList.pdf`.

## Aim

Practice abstract classes, inheritance, constructors with `super`, overriding, runtime polymorphism and pattern matching with `instanceof`.

## Source Files

Part A shapes: [`Shape.java`](./pra1/Shape.java), [`Circle.java`](./pra1/Circle.java), [`Rectangle.java`](./pra1/Rectangle.java), [`Triangle.java`](./pra1/Triangle.java), [`ShapeAreaDemo.java`](./pra1/ShapeAreaDemo.java).

Part A payroll: [`Employee.java`](./pra1/Employee.java), [`FullTime.java`](./pra1/FullTime.java), [`PartTime.java`](./pra1/PartTime.java), [`Intern.java`](./pra1/Intern.java), [`PayrollDemo.java`](./pra1/PayrollDemo.java).

Part B: [`Account.java`](./pra2/Account.java), [`SavingsAccount.java`](./pra2/SavingsAccount.java), [`CurrentAccount.java`](./pra2/CurrentAccount.java), [`FixedDepositAccount.java`](./pra2/FixedDepositAccount.java), [`MiniBankPolymorphismDemo.java`](./pra2/MiniBankPolymorphismDemo.java).

## Compile and Run

Run each command from the workspace root:

```powershell
cd Lab-5\pra1
javac *.java
java ShapeAreaDemo
java PayrollDemo
```

Run the MiniBank polymorphism demo:

```powershell
cd ..\pra2
javac *.java
java MiniBankPolymorphismDemo
```

JDK 17 or newer is required.

## Expected Checks

- Shape areas are printed with a running total, followed by the largest shape. The sample areas are approximately 12.57 (circle), 12.00 (rectangle), and 15.00 (triangle), totaling 39.57.
- Payroll prints each employee's salary, marks only the intern, and totals 86,000.00.
- The account array reports interest rates 4.0, 0.0 and 7.0 for savings, current and fixed-deposit accounts. Savings cannot withdraw below its minimum; current accounts can use their overdraft limit; fixed deposits reject withdrawals.

## Explanation

Each shape and employee is processed through its abstract base type, so Java dispatches `area()` or `monthlySalary()` to the right subclass at runtime. Each bank subtype initializes the shared account data with `super(...)` and supplies its own interest and withdrawal policy. The main loop uses one `Account[]` and invokes subtype methods polymorphically.

## Word Submission

Prepare `25CS104_MeetTanti_Practical5.docx` with the program title and problem summary, complete Java source code, and actual VS Code execution screenshots. Use Consolas or Courier New for code and include your own GitHub repository URL in the footer once that repository exists. No screenshots or Word document have been fabricated or created here.

Capture real output by running both Part A demos and the Part B demo in the VS Code terminal and using Windows Snipping Tool (`Win`+`Shift`+`S`).
