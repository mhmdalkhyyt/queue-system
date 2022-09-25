# TODO fixa så att flera ID kan hanteras
import time
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
                self.queue_box.insert(END, str(i) + ': ' + x.getName() + '\n')
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


class QueuePerson():
    def __init__(self, tnr, name, inID, type_bool):
        self.Ticket = tnr
        self.Name = name
        self.ID = inID  # flera ID
        self.Supervisor = type_bool
        self.heartbeat_time = time.time()

    def getTicketb(self):
        tempo = {"name": self.Name, "ticket": self.Ticket}
        self.sendprepr = json.dumps(str(tempo))
        return [bytes(self.sendprepr, 'UTF-8')]

    def getTicketNumber(self):
        return self.Ticket

    def getTicket(self):
        tempo = {'name': self.Name, 'ticket': self.Ticket}
        # self.sendprepr = json.dumps(tempo)
        return [self.sendprepr]

    def getID(self):
        return self.ID

    def getName(self):
        return self.Name

    def getHeartbeat(self):
        return self.heartbeat_time

    def setHeartbeat(self):
        self.heartbeat_time = time.time()


def heartbeat():
    while True:
        for t in help_queue:
            if t.getHeartbeat() < (time.time() - 10.0):
                print(t.getName() + ' är utkickad')
                help_queue.remove(t)
                gui.update_queue(help_queue)

            elif t.getHeartbeat() < (time.time() - 5.0):
                send_service([t.getID(), b""])

            else:
                pass


context = zmq.Context()
backend_socket = context.socket(zmq.ROUTER)
# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
backend_socket.bind('tcp://127.0.0.1:7000')


def send_service(mesg):
    backend_socket.send_multipart(mesg)


def send_queue():
    sendlist = list()
    for x in help_queue:
        name = x.getName()
        ticket_number = x.getTicketNumber()
        sss = {'name': name, 'ticket': ticket_number}
        sendlist.append(sss)

    send_dict = {'queue': sendlist}
    json_sendlist = json.dumps(send_dict)
    print(json_sendlist)
    for queuer in subscribers:
        send_service([queuer, bytes(json_sendlist, 'UTF-8')])


# ------------------------------------------
queueDict = dict()
subscribers = list()
help_queue = list()
supervisors = list()
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
            help_queue.append(QueuePerson(ticketNumber, user, ID, False))
            ticketNumber = ticketNumber + 1
            send_service(help_queue[0].getTicketb())
            gui.update_queue(help_queue)
            send_queue()

    elif "" in msg[1]:
        for a in help_queue:
            if ID == a.getID():
                print('got heartbeat from ' + msg[0])
                a.setHeartbeat()

    elif "supervisor" in msg[1]:
        supervisors.append(QueuePerson(ticketNumber, user, ID, True))

    elif "attend" in msg[1]:
        if ID in supervisors:
            q = help_queue.pop(0)
            send_service([ID, q.getTicketb()])
            gui.update_queue(help_queue)

    #
    #     x = [e['name'] for e in message['queue']]
    #     # gui.update_queue(x)
    #
    # else:
    #     print('got heartbeat')
    #     #socket.send_json('')

# # source venv/bin/activate
