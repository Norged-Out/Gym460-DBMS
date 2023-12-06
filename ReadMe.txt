Team: Nathan Lucero, Priyansh Nayak, Utkarsh Upadhyay, Bhargav Gullipelli

Compilation and Execution Instructions:
	Put all the files in the same directory. The entities package acts as a 
	  subdirectory and utility for the core classes: Gym460, Validation,
	  DataManipulation, and QueryManager.
	
	In order to run the DBMS, simply compile everything and then run the
	  Gym460 class with no arguments like 'java Gym460'
	 
	Note: TableGen.java is not part of the DBMS process and instead acted
			as a tool for the testing phase. Does not need compiling.
			
	List of all necessary files:
		Gym460.java
		Validation.java
		DataManipulation.java
		QueryManager.java
		entities
			Trainer.java
			Tier.java
			Course.java
			Package.java
			Member.java
			Equipment.java
			Transaction.java
			
Workload Distribution:
	The creation of all the Relations and their relationships with one
	another, designing the ER-model, and converting them to a schema
	were all the tasks done as a group together. From there, we 
	chose to split our work amongst the members to have everyone focus
	on an individual core aspect of the DBMS as follows:
	
	Priyansh Nayak:
		Created the SQL code for creating the Trainer, Member, and 
		  Transaction tables.
		Populated the Trainer table with default data.
		Created a java file called TableGen.java to facilitate Table 
		  creation whenever necessary.
		Designed Gym460.java, the core component of the DBMS, and 
		  devised the connections with other assisting classes
		  to implement the DBMS.
		Collaborated with the team to negotiate necessities and
		  structure the procedures required to implement all the
		  specifications for the DBMS.
		Designed and implemented query #4 as the additional
		  query for the querying component of the DBMS
		Acted as Project Manager to organize files and supervise
		  work distribution to ensure everyone achieves deadlines
		  on time and can work at a consistent pace.
		Modified and revised schemas throughout the progression of
		  testing to guarantee seamless execution and reflected
		  those changes in the final design pdf.
		Thoroughly tested all procedures to ensure the specifications
		  dictated by the project description are met, and to ensure
		  that all edge cases are accounted for so that the user is
		  presented with a straightforward interface to work with.
		
	
	Nathan Lucero:
		Created the SQL code for creating the Tier and Equipment tables.
		Populated the Tier and Equipment tables with default data.
		Created the Normalisation Analysis which included the Functional
		  Dependencies, closures, minimal covers, and normalisations of
		  the first, second, third, and Boyce-Codd Normal Form.
		Created the java classes for all the table entities to utilize
		  for the sake of validation procedures.
		Wrote Validation.java methods for validating data and 
		  collaborated with the team on how to use them to properly 
		  edit the database. The file existed as a safeguard between
		  the user and the database.
		Additionally helped structured the procedures for all the 
		  functionality offered by the DBMS.
		Designed and implemented queries #2 and #3 for the querying
		  component of the DBMS, developing helper methods to create
		  desired output in an understandable manner.
	
	Utkarsh Upadhyay:
		Created the SQL code for creating the Course and Package tables.
		Wrote DataManipulation.java methods to act as the gateway for 
		  manipulating the database to reflect the changes made by a user.
		Implemented member insertion and deletion functionality to enable
		  the user to add and delete members from the database as needed.
		Implemented course insertion and deletion capabilities, enabling
		  the addition and removal of gym courses as needed.
		Implemented package insertion, deletion, and updating functionality,
		  allowing users to manage gym membership packages efficiently.
		Implemented payment processing, ensuring that member payments 
		  are accurately recorded and reflected in their account balances.
		Created equipment borrowing and returning functionality, and 
		  reflecting the equipment status in the database.
		Collaborated with the team to ensure that these features seamlessly
		  integrated into the Gym460 application, enhancing its overall
		  functionality and usability.
	
	Bhargav Gullipelli:
		Collaborated with the team to ensure that all relevant methods
		  are available to facilitate operations and that everyone
		  can work without interruptions. 		  
		Created QueryManager methods to assist in printing required
		  information for the user in an organized fashion.
		Additionally created methods that return the objects made
		  using the table entities to facilitate validation procedures.
		Effectively tested all QueryManager methods to ensure that 
		  all validation and data manipulation requirements were met
		  and that the user was not presented with unnecessary errors.