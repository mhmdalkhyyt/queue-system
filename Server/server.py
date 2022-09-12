import tkinter
import zmq
import os
import queue
import binascii
from tkinter import *
import threading


class GUI(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.queue_box = None
        self.send_button = None
        self.root = None
        self.start()

    def callback(self):
        os._exit(0)

    def update_queue(self, d):
        pass

    def queue_button(self):
        pass

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)
        self.root.title("Queue server")
        self.root.geometry('500x600')

        # self.send_button = tkinter.Button(self.root, text='Remove next in queue', font=('Arial', 18),
        # command=self.queue_button)
        # self.send_button.pack(padx=20, pady=20)

        self.queue_box = tkinter.Text(self.root, font=('Arial', 16))
        self.queue_box.pack(padx=20)

        self.root.mainloop()


help_queue = list
context = zmq.Context()

# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
socket = context.socket(zmq.ROUTER)
socket.bind('tcp://127.0.0.1:7000')
# ------------------------------------------
gui = GUI()
subscribers = list

while True:
    msg = socket.recv_multipart()

    if 'subscribe' in msg[1].decode('ascii'):
        if msg[0] in subscribers():

            print('already in subs')
        else:
            subscribers.append(msg[0])
            print('Added to subs')
            # for x in range(len(subscribers())):
            #     print(subscribers[x])
            print(*subscribers)
            print('................')

    elif 'enterQueue' in msg[1].decode('ascii'):
        if msg[0] in help_queue():
            print('already in queue')
        else:
            print('Added to queue')
            help_queue().append(msg[0])
            message = msg[1].decode('ascii')
            print(type(message))
            print('................')

    # print(type(msg[0]))
    # print(type(msg[1]))
    # print(msg[0])
    # print(msg[1].decode('ascii'))

    #
    # if
    #     print('got ticket')
    #     print(message, sep='\n')
    #
    # elif 'subscribe' in message:
    #     print('got queue')
    #
    #     x = [e['name'] for e in message['queue']]
    #     # gui.update_queue(x)
    #
    # else:
    #     print('got heartbeat')
    #     #socket.send_json('')

# # source venv/bin/activate
