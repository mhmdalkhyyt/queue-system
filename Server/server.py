import tkinter
from time import sleep

import zmq
import os
import binascii
import json
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
        i = 1
        if self.queue_box is not None:
            self.queue_box.delete(1.0, END)

            for x in d:
                sliced_string = x[1]
                sliced_json = json.loads(sliced_string)
                self.queue_box.insert(END, str(i) + ': ' + sliced_json['name'] + '\n')
                i = i + 1

    def queue_button(self):
        pass

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)
        self.root.title("Queue server")
        self.root.geometry('500x600')
        self.queue_box = tkinter.Text(self.root, font=('Arial', 16))
        self.queue_box.pack(padx=20)

        self.root.mainloop()


class QueueStudent():
    def __init__(self, tnr, name, id):
        self.Ticket = tnr
        self.Name = name
        self.ID = id  # flera ID

    def getTicket(self):  # metod utan ID
        tempo = {"ticket": self.Ticket, "name": self.Name}
        self.sendprepr = json.dumps(str(tempo))
        return [self.ID, bytes(self.sendprepr, 'UTF-8')]


class QueueList:

    def __init__(self):
        self.listOfQueue = list()

    def addToQueue(self, student):
        self.listOfQueue.append(student)


def heartbeat():
    while (True):
        sleep(5)
        print('From heartbeat')


context = zmq.Context()
backend_socket = context.socket(zmq.ROUTER)
# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
backend_socket.bind('tcp://127.0.0.1:7000')


def send_service(mesg):
    backend_socket.send_multipart(mesg)


def string_creator(mesg):
    concMessage = '1wd'
    return concMessage


def send_queue():
    for queuer in subscribers:
        send_service(queuer, )


# ------------------------------------------
queueDict = dict()
subscribers = list()
help_queue = list()
ticketNumber = 1
gui = GUI()
heartThread = threading.Thread(target=heartbeat)
heartThread.start()

while True:
    msg = backend_socket.recv_multipart()
    ID = msg[0]
    msg_temp = msg[0]
    msg[0] = binascii.hexlify(msg_temp).decode('ascii')
    msg_temp = msg[1]
    msg_temp = msg_temp.decode('ascii')
    msg[1] = json.loads(msg_temp)
    print(msg)

    if "subscribe" in msg[1]:
        if ID in subscribers:
            print('already in subs')
        else:
            subscribers.append(ID)
            print(msg[0] + ' added to subs')
            print(subscribers)
            print('................')

    elif 'enterQueue' in msg[1]:
        if msg[1] in help_queue:
            print('already in queue')
        else:
            user = msg[1]['name']
            print(user + ' added to queue')
            help_queue.append(QueueStudent(ticketNumber, user, ID))
            ticketNumber = ticketNumber + 1
            send_service(help_queue[0].getTicket())
            # sendprep = json.dumps(str(temp_dict))
            # send_service([ID, bytes(sendprep, 'UTF-8')])
            # sendList = {"queue", [{"name": "gibbe", "ticket": 15}]}
            # sendprep = json.dumps(str(queueDict))
            # send_service([ID, bytes(sendprep, 'UTF-8')])

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
