# queue-system

The server and the supervisor are programmed in python and the client in java.
For testing purposes there are also a working python client that is started in the 
same way as the supervisor

## Server:
    Are located in queue-system\Server

    Use venv as an virtual environment
    
    * .\venv\bin\activate         for windows
    
    * source venv/bin/activate    for linux
    
    use the port as parameter for example:    
    python ./server.py 7000
        or
    py .\server.py 7000
---
## Supervisor:
    Are located in queue-system\Server

    Use venv as an virtual environment

    * .\venv\bin\activate       for windows

    * source venv/bin/activate    for linux
    
    use the ports of all servers as parameter for example:    
    python ./server.py 7000 7001 7002 7003
        or
    py .\server.py 7000 7001 7002 7003

---
## Client:
    text