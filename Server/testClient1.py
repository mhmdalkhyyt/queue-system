import tkinter

import zmq
from tkinter import *
import threading


class GUI(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.queue_box = None
        self.send_button = None
        self.name_box = None
        self.label = None
        self.root = None
        self.start()

    def callback(self):
        self.root.quit()
        self.root.destroy()

    def update_queue(self, d):
        self.queue_box.delete(1.0, END)
        for x in d:
            self.queue_box.insert(END, x + '\n')
        print(d)

    def queue_button(self):
        name = self.name_box.get(1.0, tkinter.END)
        name = name[:-1]
        socket.send_json({'enterQueue': True, 'name': name})

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)
        self.root.title("Queue client")
        self.root.geometry('500x800')
        self.label = tkinter.Label(self.root, text='Enter your name:', font=('Arial', 18))
        self.label.pack(padx=20, pady=20)

        self.name_box = tkinter.Text(self.root, height=1, width=15)
        self.name_box.pack(padx=20)

        self.send_button = tkinter.Button(self.root, text='Get in the queue', font=('Arial', 18),
                                          command=self.queue_button)
        self.send_button.pack(padx=20, pady=20)

        self.queue_box = tkinter.Text(self.root, font=('Arial', 16))
        self.queue_box.pack(padx=20)
        self.root.mainloop()


context = zmq.Context()
socket = context.socket(zmq.DEALER)

socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
socket.send_json({'subscribe': True})
gui = GUI()


while True:
    message = socket.recv_json()

    if 'ticket' in message:
        print('got ticket')
        print(message, sep='\n')

    elif 'queue' in message:
        print('got queue')

        x = [e['name'] for e in message['queue']]
        gui.update_queue(x)

    else:
        print('got heartbeat')
        socket.send_json('')


# # source venv/bin/activate
