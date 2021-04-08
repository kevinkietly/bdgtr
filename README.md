# bdgtr

bdgtr is a personal finance application that helps you create budgets and track your spending.

## Features

Create **unlimited**:
* accounts
* budgets
* categories
* transactions

View:
* account overview
* budget breakdown
* transaction history

## User Stories

As a user, I want to be able to:
* sign up for an account
* sign in to my account (load from file)
* save my changes to file
* have the option to save my changes to file when I quit the application
* add a budget to my account
* delete a budget from my account
* select a budget and add a category to that budget
* select a budget and delete a category from that budget
* select a category in a budget and add a transaction to that category
* select a category in a budget and delete a transaction from that category
* view my account overview
* view the breakdown of a budget
* view my transaction history

## Phase 4: Task 2

I chose to make my code more robust.
The constructor for the Account class throws four checked exceptions:
* EmptyFirstNameException
* EmptyLastNameException
* EmptyUsernameException
* EmptyPasswordException

## Phase 4: Task 3

If I had more time to work on the project, I would refactor:
* Account, Budget, and Category
    * These classes have similar add and delete methods,
      so I would create an abstract class with these methods and make these classes extend the abstract class.
* Bdgtr and EntryPanel
    * These classes have similar methods to handle closing/signing out,
      so I would create an abstract class with these methods and make these classes extend the abstract class.
* EntryPanel
    * This class has almost identical methods to initialize different fields,
      so I would create a general method to initialize a field that would take in the appropriate parameters.