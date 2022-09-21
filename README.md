# coding-hours

The main purpose of this program is to let users log their coding hours for certain dates. 
Each entry will contain a date and hours for that date.
The user will be given five options, Add entry, remove entry, update entry, view logged entries, and quit out of the program.
All of the entry options are linked to the database, so if an entry is added, it will appear inside the table in CodingHours.

This program uses MSSQL(SQL Server) with JDBC and Spring Framework. To modify the database information (URL and login), input them into application.properties

When this program is ran, it will check if there is a database named CodingHours, if not it will create one.
Then it checks for the Table named t, if not it will also create a table within CodingHours.

Options
1. Add: User is requested to enter a date and hours. The program checks that these are in the right format and then inputs them into the database.
2. Delete: User is requested to enter a date. The program checks this date in the database and then deletes that entry.
3. Update: User is requested to enter a date. The program checks this date in the database and then requests for the hours that needs to be updated. It then takes that number and updates it into the database for that entry. 
4. View: The program pulls all the information from the database and displays them to the user. If no entries are found, it will also say so. 
When the user is done, they can type 0 to quit out of the program. 
