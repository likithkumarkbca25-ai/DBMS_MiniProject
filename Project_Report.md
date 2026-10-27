# Game Collection Management System
## Mini Project Report | Java Swing and MySQL

### 1. Introduction
The Game Collection Management System is a desktop application for maintaining a personal video-game collection. It stores each game's title, platform, genre, price and play status in MySQL. JDBC connects the Java application to the database, allowing records to persist after the application closes.

### 2. Objectives
- Build a graphical interface using Java Swing.
- Create a MySQL database and table.
- Implement INSERT, SELECT, UPDATE and DELETE.
- Add search, input validation and deletion confirmation.
- Demonstrate real JDBC connectivity and persistent storage.

### 3. Tools and Technologies
- Java 17+: application logic
- Swing: desktop user interface
- MySQL: persistent storage
- JDBC / MySQL Connector/J: database connectivity
- Maven: build and dependency management
- MySQL Workbench: database administration and verification

### 4. Database Design
Database: `game_collection_db`; table: `games`.

| Column | Type | Purpose |
|---|---|---|
| game_id | INT, primary key, auto increment | Unique identifier |
| title | VARCHAR(100), required | Game title |
| platform | VARCHAR(50), required | Platform |
| genre | VARCHAR(50) | Genre |
| price | DECIMAL(10,2) | Price in rupees |
| status | ENUM | Not Played, Playing, Completed, On Hold |
| created_at | TIMESTAMP | Record creation time |

### 5. Features
- Add a game: INSERT
- View and search games: SELECT
- Update selected game: UPDATE
- Delete selected game after confirmation: DELETE
- Validation for required fields, text lengths, and non-negative prices

### 6. How to Run
1. Install JDK 17+, MySQL Server, and Maven, or use a Maven-capable IDE.
2. Execute `database/game_collection_db.sql` in MySQL Workbench.
3. Update credentials in `src/main/resources/db.properties`.
4. Run `mvn clean compile` from the project root.
5. Run `com.gamecollection.GameCollectionApp` from your IDE.
6. Test each CRUD operation and verify results in MySQL Workbench.

### 7. Testing and Results
Fill in actual observed results after testing.

| Test | Expected result | Observed result |
|---|---|---|
| Add valid game | Record appears in app and MySQL | To be filled |
| View all games | Saved records appear | To be filled |
| Update selected game | Changes persist | To be filled |
| Delete selected game | Record is removed | To be filled |
| Invalid input | Validation message appears | To be filled |
| Restart application | Records remain saved | To be filled |

### 8. Conclusion
This project demonstrates Java Swing, JDBC, SQL and persistent database storage through a working CRUD application. Possible future enhancements include login, game cover images, advanced filters and CSV export.

### 9. Screenshot Checklist
Capture actual screenshots of:
1. Main app with games displayed.
2. Adding a new game and the success message.
3. MySQL Workbench showing the inserted row.
4. Updated record in the app and database.
5. Deleted record no longer appearing.
6. Optional search and validation messages.

Replace test placeholders with actual results and insert screenshots before submission. Do not use mock screenshots.
