import zmq

context = zmq.Context()
socket = context.socket(zmq.SUB)
socket.connect('tcp://127.0.0.1:7000')
socket.setsockopt_string(zmq.SUBSCRIBE, '')

while(True):
    message = socket.recv_pyobj()
    print (message)
