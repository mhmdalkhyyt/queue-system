import tkinter
from time import sleep
import zmq
import sys
import os
from tkinter import *
from tkinter import messagebox
import threading


class GUI(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.queue_box = None
        self.su_box = None
        self.send_button = None
        self.name_box = None
        self.first_label = None
        self.second_label = None
        self.root = None
        self.start()

    def callback(self):
        os._exit(0)

    def update_queue(self, d):

        if self.queue_box is not None:
            self.queue_box.delete(1.0, END)
            user_name = self.name_box.get(1.0, tkinter.END)
            user_name = user_name[:-1]
            i = 1
            for x in d:
                if x == user_name:
                    self.queue_box.insert(END, str(i) + ': ' + x + '           <----- You' + '\n')
                else:
                    self.queue_box.insert(END, str(i) + ': ' + x + '\n')
                i = i + 1

    def update_supervisors(self, d):

        if self.su_box is not None:
            self.su_box.delete(1.0, END)
            user_name = self.name_box.get(1.0, tkinter.END)
            user_name = user_name[:-1]
            i = 1
            for x in d:
                if x == user_name:
                    self.su_box.insert(END, str(i) + ': ' + x + '           <----- You' + '\n')
                else:
                    self.su_box.insert(END, str(i) + ': ' + x + '\n')
                i = i + 1

    def queue_button(self):
        name = self.name_box.get(1.0, tkinter.END)
        name = name[:-1]
        socket.send_json({'enterQueue': True, 'name': name})
        heart_thread = threading.Thread(target=heartbeat)
        heart_thread.start()

    def show_attend_msg(self, string):
        messagebox.showinfo(title='Supervisor is here', message=string)

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)
        self.root.title("Queue client")
        self.root.geometry('500x800')
        self.first_label = tkinter.Label(self.root, text='Enter your name:', font=('Arial', 18))
        self.first_label.pack(padx=20, pady=20)

        self.name_box = tkinter.Text(self.root, height=1, width=15)
        self.name_box.pack(padx=20)

        self.send_button = tkinter.Button(self.root, text='Get in the queue', font=('Arial', 18),
                                          command=self.queue_button)
        self.send_button.pack(padx=20, pady=10)

        self.queue_box = tkinter.Text(self.root, height=18, font=('Arial', 16))
        self.queue_box.pack(padx=20, pady=10)

        self.second_label = tkinter.Label(self.root, text='Supervisors:', font=('Arial', 18))
        self.second_label.pack(padx=20)

        self.su_box = tkinter.Text(self.root, height=4, font=('Arial', 16))
        self.su_box.pack(padx=20, pady=20)

        socket.send_json({'subscribe': True})
        self.root.mainloop()


context = zmq.Context()
socket = context.socket(zmq.DEALER)


def heartbeat():
    while (True):
        sleep(3)
        socket.send_json('')


arg = sys.argv

# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
socket.connect('tcp://127.0.0.1:' + arg[1])
# ------------------------------------------
gui = GUI()

while True:
    message = socket.recv_json()
    if 'ticket' in message:
        print('got ticket')
        print(message, sep='\n')

    elif 'queue' in message:
        print('got queue')
        print(message)
        x = [e["name"] for e in message['queue']]
        gui.update_queue(x)

    elif 'supervisors' in message:
        print('got supervisors')
        print(message)
        x = [e["name"] for e in message['supervisors']]
        gui.update_supervisors(x)

    elif 'attending' in message:
        print('attended')
        gui.show_attend_msg(message['message'])
        print(message)

# # source venv/bin/activate
