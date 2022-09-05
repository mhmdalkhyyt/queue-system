import zmq

context = zmq.Context()
socket = context.socket(zmq.DEALER)
socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')

socket.send_json({'subscribe': True})

socket.send_json({'enterQueue': True, 'name':'Testnamn'})

message = socket.recv_json()

print(message)
# socket.setsockopt_string(zmq.SUBSCRIBE, '')

# thisName = {
#    "enterQueue": "true",
#    "name": "<Testnamn>"
#}


#def send_name():
#    socket.send_json(thisName)

#
# while (True):
#     message = socket.recv_pyobj()
#     print(message)
