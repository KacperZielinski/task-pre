## How to run it? 
(in parent folder)

	Compile
	mvn clean install -DskipTests
	
	Run
	mvn spring-boot:run -Dspring-boot.run.jvmArguments=-Ddirectory.source=[#folder_name]

	
	Example:
	mvn clean install -DskipTests
	mvn spring-boot:run -Dspring-boot.run.jvmArguments=-Ddirectory.source=C:\RDev\FOLDER\
