import tkinter
import zmq
import os
import queue
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
        self.root.geometry('500x800')

        # self.send_button = tkinter.Button(self.root, text='Remove next in queue', font=('Arial', 18),
                                          # command=self.queue_button)
        # self.send_button.pack(padx=20, pady=20)

        self.queue_box = tkinter.Text(self.root, font=('Arial', 16))
        self.queue_box.pack(padx=20)

        self.root.mainloop()


help_queue = queue.Queue()
context = zmq.Context()
socket = context.socket(zmq.ROUTER)

# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
socket.bind('tcp://127.0.0.1:7000')
# ------------------------------------------
gui = GUI()

while True:
    message = socket.recv_json()
    print('received')
    print(message)

    if 'enterQueue' in message:
        print('got ticket')
        print(message, sep='\n')

    elif 'subscribe' in message:
        print('got queue')

        x = [e['name'] for e in message['queue']]
        # gui.update_queue(x)

    else:
        print('got heartbeat')
        #socket.send_json('')

# # source venv/bin/activate
