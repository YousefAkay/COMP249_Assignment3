SmartTravel System - Assignment 3 Extension

A2 Compatibility: Y

Project Notes
- This project extends the working Assignment 2 SmartTravel codebase directly.
- Menus 1-6 and 8-11 remain available for Assignment 2 compatibility and Assignment 3 demonstrations.
- Main menu option 7 opens the Assignment 3 Advanced Analytics submenu.
- Main menu option 10 runs the Assignment 3 persistence scenario demo.
- Sample/input CSV data is loaded from data/.
- Generated/current CSV output is saved to output/data/.

RecentList<T> LinkedList Justification
- RecentList<T> inserts each new item at the front and removes the oldest item from the end, so LinkedList fits the required addFirst() and removeLast() behavior cleanly.
- This keeps the recent-trip history implementation simple to explain in a TA demo while staying bounded to 10 items.

A3 Additions
- Collection-backed service layer using ArrayList for main entity storage.
- Generic Repository<T> for add, findById, filter, and natural sorting.
- Generic RecentList<T> for recent trip history.
- GenericFileManager for Assignment 3 CSV persistence support.
- Natural Comparable ordering for clients, trips, accommodations, and transportations.
