# An Asynchronous Java RMI String Comparison Service
The project is a Java RMI String comparison service, which is meant to get user input from web browser and calculate the distance between the given string by the choosen algorithm.

## Project Structure
The project has 3 core part:
- **Web service**: handles requests from web browser and dispatches them to a remote execution handler
- **Remote execution handler**: handles the remote executions trough RMI and keeps references to objects in/out execution
- **Remote execution service**: handles the executions via a thread pool

### Project structural diagram
