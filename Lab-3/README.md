# Practical 3 - Object Equality and MiniBank Object Behavior

**Student:** Meet Tanti  
**Student ID:** 25CS104  
**Subject:** Java Programming  
**Official course:** Object-Oriented Programming (CEUE203)

## Problem Summary

Part A checks equality of points by coordinates and detects the first repeated card by rank and suit. Part B makes bank accounts printable and comparable by account number, adds a nested address type to customers, and clones a customer. The optional advanced fraction exercise is marked for home and was not included.

The official source is the practical-list PDF supplied at `C:\Users\meett\Downloads\2026-27-ODD-CEUE203-OOPJ-PracticalList.pdf`.

## Aim

Practice overriding `toString`, `equals` and `hashCode`, comparing objects by value, using a static nested class, cloning, and checking an object's type with `instanceof`.

## Source Files

- [`Point.java`](./pra1/Point.java)
- [`Card.java`](./pra1/Card.java)
- [`Driver.java`](./pra1/Driver.java) runs both required Part A examples to avoid duplicate `Driver` classes in one folder.
- [`Account.java`](./pra2/Account.java)
- [`Customer.java`](./pra2/Customer.java)
- [`MiniBankObjectDemo.java`](./pra2/MiniBankObjectDemo.java)

## Compile and Run

From the workspace root:

```powershell
cd Lab-3\pra1
javac *.java
java Driver
```

Run the MiniBank object demonstration:

```powershell
cd ..\pra2
javac *.java
java MiniBankObjectDemo
```

JDK 17 or newer is required.

## Sample Checks

Part A prints:

```text
Distinct: 3
Duplicate found: Ace of Spades
```

Part B prints an account as `AC0001 | Riya | 4000`, reports equality and matching hash codes as `true`, and confirms the object is an `Account`. It also prints the customer's address and confirms the clone is a separate object with the same customer ID.

## Explanation

`Point.equals` compares both coordinates, and `Card.equals` compares rank and suit; their `hashCode` implementations use the same fields as their equality checks. `Account.equals` compares account numbers, while `toString` formats the account number, owner and balance. `Customer.Address` is a static nested class with getters. `Customer.clone` uses `Object.clone` to return a shallow copy; the address object is immutable.

The package-private account copy constructor in the demo creates a distinct object with the same account number so value-based account equality can be demonstrated without allowing callers to assign account IDs through the public constructors.

## Word Submission

Prepare `25CS104_MeetTanti_Practical3.docx` with the program title and problem summary, complete Java source code, and actual VS Code execution screenshots. Use Consolas or Courier New for code and include your own GitHub repository URL in the footer once that repository exists. No screenshots or Word document have been fabricated or created here.

Capture real output by running both programs in the VS Code terminal and using Windows Snipping Tool (`Win`+`Shift`+`S`).
