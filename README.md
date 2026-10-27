# Game Collection Management System

A beginner-friendly Java Swing mini project connected to a real MySQL database using JDBC. The application supports **INSERT, SELECT, UPDATE, and DELETE**, plus searching and basic validation.

## Features
- Add a game with title, platform, genre, price, and play status.
- Display saved games in a table.
- Search by title, platform, or genre.
- Select a row to load it into the form.
- Update or delete the selected game.
- Input validation and confirmation before deletion.
- Database errors are shown to the user.

## Requirements
- JDK 17 or newer
- MySQL Server 8.0 or compatible
- MySQL Workbench (recommended)
- Maven 3.8+ (the MySQL Connector/J dependency is downloaded by Maven)
- Internet connection for the first Maven build to download the JDBC driver

## Setup

### 1. Create the database
1. Start MySQL Server.
2. Open MySQL Workbench and connect to your local server.
3. Open `database/game_collection_db.sql`.
4. Execute the whole script.
5. Run `SELECT * FROM game_collection_db.games;` to verify the sample rows.

The script creates the database and table, then adds sample rows only when the table is empty.

### 2. Configure the connection
Open `src/main/resources/db.properties` and set your local MySQL username and password:

```properties
db.url=jdbc:mysql://localhost:3306/game_collection_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_MYSQL_PASSWORD
```

Replace `YOUR_MYSQL_PASSWORD` with the password you created for your local MySQL account. Do not submit a real password in a public repository. If your MySQL server uses another port, update `3306`.

### 3. Run the application
Open a terminal in the project folder (the folder containing `pom.xml`) and run:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.gamecollection.GameCollectionApp"
```

If Maven's exec plugin is not available in your environment, use this alternative:

```bash
mvn clean package
java -cp "target/classes;%USERPROFILE%\\.m2\\repository\\com\\mysql\\mysql-connector-j\\9.5.0\\mysql-connector-j-9.5.0.jar" com.gamecollection.GameCollectionApp
```

The alternative classpath above is for Windows. On macOS/Linux, replace the semicolon `;` with a colon `:` and use the appropriate home-directory path. The first `mvn clean package` downloads the JDBC dependency.

Recommended simple option: import the folder as an existing Maven project in IntelliJ IDEA or Eclipse, wait for dependencies to load, then run `GameCollectionApp.java`.

## How to use
1. **Add:** Fill in title, platform, price, and optional genre; choose status; click Add Game.
2. **View:** The table loads records from MySQL at startup. Click Show All to clear a search.
3. **Search:** Enter part of a title, platform, or genre and click Search.
4. **Update:** Select a table row, edit its values in the form, then click Update Selected.
5. **Delete:** Select a row, click Delete Selected, and confirm.
6. **Verify persistence:** Close and reopen the app, or run `SELECT * FROM games;` in Workbench.

## Validation
- Title and platform cannot be blank.
- Title is limited to 100 characters.
- Platform and genre are limited to 50 characters.
- Price must be a valid non-negative number with at most two decimal places.
- Updating/deleting requires a selected row.
- Deletion requires confirmation.
- Prepared statements are used for database operations.

## Project structure
```text
GameCollectionManagement/
├── pom.xml
├── README.md
├── database/
│   └── game_collection_db.sql
├── docs/
│   ├── Project_Report.docx
│   └── Screenshot_Checklist.md
└── src/main/
    ├── java/com/gamecollection/
    │   ├── Database.java
    │   ├── Game.java
    │   ├── GameRepository.java
    │   └── GameCollectionApp.java
    └── resources/
        └── db.properties
```

## Troubleshooting
- **Access denied:** Check username/password in `db.properties`.
- **Communications link failure:** Ensure MySQL Server is running and port 3306 is correct.
- **Unknown database:** Run the SQL script first.
- **Driver not found:** Build with Maven and allow it to download dependencies.
- **No records visible:** Click Show All and verify the database/table name.

## Database operations for viva
- `INSERT`: `GameRepository.add()`
- `SELECT`: `GameRepository.findAll()`
- `UPDATE`: `GameRepository.update()`
- `DELETE`: `GameRepository.delete()`

## Notes
This is an educational local application. The connection settings are for local development; do not expose your MySQL server publicly or commit real credentials.
