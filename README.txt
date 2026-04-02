SmartTravel System - Assignment 3 Extension

A2 Compatibility: Y

Project Notes
- This project extends the working Assignment 2 SmartTravel codebase directly.
- Menus 1-6 and 8-11 are preserved for Assignment 2 compatibility.
- Main menu option 7 is replaced with the Assignment 3 Advanced Analytics submenu.
- Sample/input CSV data is loaded from data/.
- Generated/current CSV output is saved to output/data/.

RecentList<T> LinkedList Justification
- RecentList<T> inserts each new item at the front and removes the oldest item from the end.
- LinkedList was chosen because the required operations are addFirst() and removeLast(), which map cleanly to the data structure and keep the implementation easy to explain in a TA demo.
- This class is bounded to a maximum of 10 items and is used for recent trip history in the analytics workflow.

A3 Additions
- Collection-backed service layer using ArrayList for main entity storage.
- Generic Repository<T> for add, findById, filter, and natural sorting.
- Generic RecentList<T> for recent trip history.
- GenericFileManager for Assignment 3 CSV persistence support.
- Natural Comparable ordering for clients, trips, accommodations, and transportations.
