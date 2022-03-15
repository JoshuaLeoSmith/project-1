# project-1


#Installation:
1. Download project 1 onto some folder. 
2. Run mvn Install while in the source folder.
3. Add 
```
<dependency>
<groupId>com.revature</groupId>
<artifactId>project-1-ORM</artifactId>
<version>0.0.1-SNAPSHOT</version>
</dependency>
```
to your project's pom.xml file. 
4. Save it
5. Run mvn install (mvn clean install)? in a terminal inside your projects file. 
6. Update mvn and refresh on your project
7. Congrats, it should now be installed on your project.  


To Use:
Any booleans are whether to immediantly commmit the command.

import com.revature.service.IServices; and com.revature.service.ServicesImpl;

create a new Iservices field:
IServices Simpl = new ServicesImpl();

#DDL
To create a class in the database:
Simpl.create(Tester.class);

To update a class in the database:
Simpl.alter(Tester.class);

To drop a class in the database:
Simpl.drop(Tester.class);

To truncate a class in the database:
Simpl.truncate(Tester.class);

To rename a class in the database:
Simpl.renameTable(Tester.class, "OldName");

To rename a column in the database:
Simpl.renameColumn(Tester.class, "OldName", "NewName");

#DML
To insert a row into the database: 
Simpl.insert(batman, true);

batman.parentsKilled=true;
To update a row into the database: 
Simpl.updateRow(batman, true);

To remove a row from the database: 
Simpl.removeByPk(Tester.class,5, true);

To remove a row from the database following some rule: 
Simpl.removeByPk(Tester.class,"age>4", true);

To find a row from  the database following some rules: 
Simpl.find(Tester.class, "first_name = 'Bob';");

To find a row from  the database using the primary key: 
Simpl.findByPk(Tester.class,3);

To undo everything since your last commit:
Simpl.rollback();

To commit everything:
Simpl.commit();
