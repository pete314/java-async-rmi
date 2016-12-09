# An Asynchronous Java RMI String Comparison Service
The project is a Java RMI String comparison service, which is meant to get user input from web browser and calculate the distance between the given string by the choosen algorithm.

## Project Structure
The project has 3 core part:
- **Web service**: handles requests from web browser and dispatches them to a remote execution handler
- **Remote execution handler**: handles the remote executions trough RMI and keeps references to objects in/out execution
- **Remote execution service**: handles the executions via a thread pool

### Project architecture
![Project Architecture Diagram](https://raw.githubusercontent.com/pete314/java-async-rmi/master/docs/RMI_project_structure.png?token=AIYB_OOu86CCd1BcoOwQMwdsl1ee2TgLks5YVE7pwA%3D%3D)

The design of the application is based on multiple queues in order to get maximum performance for each service. The webserver is handles the tasks trough a monitoring thread which takes tasks from execution local queue. These elements are sent to be executed as object references to the remote execution service. The remote execution service maintains a thread pool for executions which is backed by a local queue. This helps to keep the maximum number of executor threads alive and enables the handling of extra loads if no free threads are available. <br>
The execution and the status is monitored trough a map which holds the execution id and the point to the executable object. This decreases the memory footprint of the application and increases the performance. <br>

**Please note that the application is slowed down on purpuse, to simulate a higher load!""

### Deployment

**Linux/Unix**<br>
Open the terminal and execute the following commands
```bash
# The following script requires wget
# Make some structure for the application
mkdir -p repo
mkdir -p web-server

# Get the repository
cd repo/
wget https://github.com/pete314/java-async-rmi/archive/master.zip
tar -xz -f master.zip
rm -rf master.zip

# Get a fresh tomcat instance
cd ../web-server
wget http://www-eu.apache.org/dist/tomcat/tomcat-7/v7.0.73/bin/apache-tomcat-7.0.73.tar.gz
tar -xz -f apache-tomcat-7.0.73.tar.gz
rm -rf apache-tomcat-7.0.73.tar.gz

# Copy the deployment into location
cd ..
cp ./repo/deploy/*.war ./web-server/apache-tomcat-7.0.73/webapps/

# Start tomcat
sh web-server/apache-tomcat-7.0.73/bin/startup.sh

printf("\n**************\n\tNavigate to http://localhost:9000/web-string-service/ \n**************\n")

```
**Manual deployment**<br>
* Open a web browser and download the repository from [here](https://github.com/pete314/java-async-rmi/archive/master.zip)
* Unzip the project
* Download an instance of tomcat (if needed) from [here](http://www-eu.apache.org/dist/tomcat/tomcat-7/v7.0.73/bin/apache-tomcat-7.0.73.zip)
* Unzip tomcat
* Navigate to ```Tomcat unziped root/webapps``` and copy the ```web-string-service.war``` file from the ```project source repository/deploy``` folder, here.
* Start tomcat by executing the ```bin/startup.*``` file 
* Navigate to ``` http://localhost:9000/web-string-service/``` to run the service web ui
