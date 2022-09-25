import tkinter
from time import sleep
import zmq
import os
from tkinter import *
import threading


class GUI(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.su_box = None
        self.queue_box = None
        self.supervise_button = None
        self.attend_button = None
        self.name_box = None
        self.first_label = None
        self.second_label = None
        self.root = None
        self.buttonframe = None
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

    def supervise_button(self):
        print('supervise button')
        name = self.name_box.get(1.0, tkinter.END)
        name = name[:-1]
        socket.send_json({'supervisor': True, 'name': name})
        heart_thread = threading.Thread(target=heartbeat)
        heart_thread.start()

    def attend_button(self):
        print('send attend')
        socket.send_json({"attend": True})

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)
        self.root.title("Supervisor client")
        self.root.geometry('500x800')
        self.first_label = tkinter.Label(self.root, text='Enter your name:', font=('Arial', 18))
        self.first_label.pack(padx=20, pady=5)

        self.name_box = tkinter.Text(self.root, height=1, width=15)
        self.name_box.pack(padx=20)

        self.buttonframe = tkinter.Frame(self.root)
        self.buttonframe.columnconfigure(0)

        self.supervise_button = tkinter.Button(self.buttonframe, text='Supervise', font=('Arial', 18),
                                               command=self.supervise_button)
        self.supervise_button.grid(row=0, column=0)

        self.attend_button = tkinter.Button(self.buttonframe, text='Attend', font=('Arial', 18), command=self.attend_button)
        self.attend_button.grid(row=0, column=1)

        self.buttonframe.pack(pady=10)

        self.queue_box = tkinter.Text(self.root, height=18, font=('Arial', 16))
        self.queue_box.pack(padx=20, pady=10)

        self.second_label = tkinter.Label(self.root, text='Supervisors:', font=('Arial', 18))
        self.second_label.pack(padx=20)

        self.su_box = tkinter.Text(self.root, height=4, font=('Arial', 16))
        self.su_box.pack(padx=20, pady=20)
        print('everything is packed')
        socket.send_json({'subscribe': True})
        socket.send_json({'supervisor': True, 'name': 'testnamn'})
        socket.send_json({"attend": True})
        self.root.mainloop()


context = zmq.Context()
socket = context.socket(zmq.DEALER)


def heartbeat():
    while (True):
        sleep(3)
        socket.send_json('')


# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
socket.connect('tcp://127.0.0.1:7000')
# ------------------------------------------
gui = GUI()
print('gui complete')


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

    elif 'supervisor' in message:
        print('got supervisors')
        print(message)
        x = [e["name"] for e in message['supervisors']]
        gui.update_queue(x)
# # source venv/bin/activate
