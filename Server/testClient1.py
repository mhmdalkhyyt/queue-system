import zmq
from time import sleep
from tkinter import *

window = Tk()
window.title("Queue client")
window.mainloop()

# source venv/bin/activate
def print_queue(queue_msg):
    print("Printing queue")
    for i in queue_msg:
        print(i)


context = zmq.Context()
socket = context.socket(zmq.DEALER)
socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')

socket.send_json({'enterQueue': True, 'name': 'Testnamn'})
message = socket.recv_json()
print(message)
socket.send_json({'subscribe': True})
queue_msg = socket.recv_json()
print_queue(queue_msg)

while True:
    message = socket.recv_json()

    if 'ticket' in message:
        print(message, sep='\n')

    elif 'queue' in message:
        # print(*message, sep='\n')
        # print(message['name'])
        print(type(message))
        for dicts in message:
            for key, value in message.items():
                print(key, ' : ', value)

    else:
        socket.send_json('')
