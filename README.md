# queue-system

The server and the supervisor are programmed in python and the client in java.
For testing purposes there are also a working python client that is started in the 
same way as the supervisor

## Server:
    Are located in queue-system\Server

    Use venv as an virtual environment
    
    .\venv\bin\activate         for windows
        or
    source venv/bin/activate    for linux
    
    use the port as parameter for example:    
    python ./server.py 7000
        or
    py .\server.py 7000
---
## Supervisor:
    Are located in queue-system\Server

    Use venv as an virtual environment

    .\venv\bin\activate         for windows

    source venv/bin/activate    for linux
    
    use the ports of all servers as parameter for example:    
    python ./server.py 7000 7001 7002 7003
        or
    py .\server.py 7000 7001 7002 7003

---
## Client:
    The studentclient is written in Java.
    The main class of the client is DSAssign1.java
    (PATH DSAssin1/src/main/java/com/dsassign1/DSAssign1.java).
    
    Running this client requires program arguments server clients tcp address + port number. 

    E.g. 
        tcp://127.0.0.1:7000

    Connection to multiple servers is also possible by adding another address to the argument array, via the program arguments. 

    E.g. 
        tcp://127.0.0.1:7000 tcp://127.0.0.1:7001

        
    HAPPY QUEUING!



Authors: Eric Pettersson[b20eripe] , Muhammed Al Khayyat  [a20muhal]


    