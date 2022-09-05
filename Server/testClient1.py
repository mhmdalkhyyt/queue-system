import zmq

context = zmq.Context()
socket = context.socket(zmq.DEALER)
socket.connect('tcp://http://tinyqueue.cognitionreversed.com/:5556')
# socket.setsockopt_string(zmq.SUBSCRIBE, '')

thisName = {
    "enterQueue": "true",
    "name": "<Testnamn>"
}


def send_name():
    socket.send_json(thisName)

#
# while (True):
#     message = socket.recv_pyobj()
#     print(message)
