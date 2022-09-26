# TODO supervisormeddelande
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
        self.time_box = None
        self.label = None
        self.button = None
        self.root = None
        self.button_frame = None
        self.s_button = None

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

    def updateKickoutTime(self):
        heartb.setKickoutTime(float(self.time_box.get(1.0, tkinter.END)))

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)
        self.root.title("Queue server")
        self.root.geometry('500x600')

        self.button_frame = tkinter.Frame(self.root)

        self.label = Label(self.button_frame, text='Select kickout time:', font=('Arial', 10))
        self.label.grid(row=0, column=0)

        self.time_box = tkinter.Text(self.button_frame, height=1, width=4)
        self.time_box.insert(1.0, '15')
        self.time_box.grid(row=0, column=1)

        self.s_button = tkinter.Button(self.button_frame, text='Change', font=('Arial', 10),
                                       command=self.updateKickoutTime)
        self.s_button.grid(row=0, column=2)

        self.button_frame.pack()
        self.queue_box = tkinter.Text(self.root, font=('Arial', 16))
        self.queue_box.pack(padx=20)

        self.root.mainloop()


class QueuePerson():
    def __init__(self, tnr, name, inID, type_bool):
        self.Ticket = tnr
        self.Name = name
        self.ID = list()
        self.ID.append(inID)  # flera ID
        self.Supervisor = type_bool
        self.heartbeat_time = time.time()

    def addID(self, addID):
        self.ID.append(addID)

    def getTicketb(self):
        tempo = {"name": self.Name, "ticket": self.Ticket}
        self.sendprepr = json.dumps(str(tempo))
        return [bytes(self.sendprepr, 'UTF-8')]

    def getTicketNumber(self):
        return self.Ticket

    def getTicket(self):
        tempo = {'name': self.Name, 'ticket': self.Ticket}
        return [self.sendprepr]

    def getID(self):
        return self.ID

    def getName(self):
        return self.Name

    def getHeartbeat(self):
        return self.heartbeat_time

    def setHeartbeat(self):
        self.heartbeat_time = time.time()
        print(self.getName())


class HeartBeat():

    def __init__(self):
        threading.Thread.__init__(self)
        self.kickouttime = 15.0

    def heartbeatQ(self):
        while True:
            if len(help_queue) > 0:
                for t in help_queue:
                    if t.getHeartbeat() < (time.time() - self.kickouttime):
                        print(t.getName() + ' är utkickad')
                        print(self.kickouttime)
                        help_queue.remove(t)
                        gui.update_queue(help_queue)
                        send_queue()

                    elif t.getHeartbeat() < (time.time() - 5.0):
                        print(t.getName() + " är för långsam")
                        for d in t.getID():
                            send_service([d, b"{}"])
                    else:
                        pass

            sleep(1)

    def heartbeatS(self):
        while True:
            if len(supervisors) > 0:
                for sup in supervisors:
                    if sup.getHeartbeat() < (time.time() - float(self.kickouttime)):
                        print(sup.getName() + ' är utkickad')
                        supervisors.remove(sup)
                        send_supervisors()

                    elif sup.getHeartbeat() < (time.time() - 5.0):
                        for d in sup.getID():
                            send_service([d, b"{}"])
                    else:
                        pass
            sleep(1)

    def startHeartBeats(self):
        heartThreadQ = threading.Thread(target=self.heartbeatQ)
        heartThreadQ.start()
        heartThreadS = threading.Thread(target=self.heartbeatS)
        heartThreadS.start()

    def setKickoutTime(self, k_time):
        print('Set time'+ str(k_time))
        self.kickouttime = k_time


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


def send_supervisors():
    sendlist_su = list()
    for x in supervisors:
        name = x.getName()
        ticket_number = x.getTicketNumber()
        sss = {'name': name, 'ticket': ticket_number}
        sendlist_su.append(sss)

    send_dict = {'supervisors': sendlist_su}
    json_sendlist = json.dumps(send_dict)
    print(json_sendlist)
    for queuer in subscribers:
        send_service([queuer, bytes(json_sendlist, 'UTF-8')])


context = zmq.Context()
backend_socket = context.socket(zmq.ROUTER)
# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
backend_socket.bind('tcp://127.0.0.1:7000')

# ------------------------------------------
queueDict = dict()
subscribers = list()
help_queue = list()
supervisors = list()
#setKickoutTime(15.0)

ticketNumber = 1
gui = GUI()
heartb = HeartBeat()
heartb.startHeartBeats()


while True:
    msg = backend_socket.recv_multipart()
    ID = msg[0]
    msg_temp = msg[0]
    msg[0] = binascii.hexlify(msg_temp).decode('ascii')
    msg_temp = msg[1]
    try:
        msg_temp = msg_temp.decode('UTF-8')
        msg[1] = json.loads(msg_temp)
    except:
        print('convertion is not possible')


    if "subscribe" in msg[1]:
        if ID in subscribers:
            print('already in subs')
        else:
            subscribers.append(ID)
            print(msg[0] + ' added to subs')
            send_queue()
            send_supervisors()

    elif "enterQueue" in msg[1]:
        enterQueue = True
        for person in help_queue:  # Om namnet redan finns läggs id:t till i listan över id som ligger på personen
            if msg[1]["name"] in person.getName():
                person.addID(ID)
                enterQueue = False
                gui.update_queue(help_queue)
                send_queue()

        if enterQueue:
            user = msg[1]["name"]
            print(user + ' added to queue')
            help_queue.append(QueuePerson(ticketNumber, user, ID, False))  # false för att det inte är en supervisor
            ticketNumber = ticketNumber + 1
            send_service(help_queue[0].getTicketb())
            gui.update_queue(help_queue)
            send_queue()

    elif "" in msg[1] or "{}" in msg[1]:
        for a in help_queue:
            if ID in a.getID():
                a.setHeartbeat()

        for a in supervisors:
            if ID in a.getID():
                a.setHeartbeat()


    elif "supervisor" in msg[1]:
        print('supervisor added')
        enterQueue = True
        for person in supervisors:
            if msg[1]["name"] in person.getName():
                person.addID(ID)
                enterQueue = False
                send_supervisors()

        if enterQueue:
            user = msg[1]["name"]
            print(user + ' added to supervisors')
            supervisors.append(QueuePerson(ticketNumber, user, ID, False))
            ticketNumber = ticketNumber + 1
            send_supervisors()

    elif "attend" in msg[1]:
        print('attending')
        for su in supervisors:
            if ID in su.getID() and len(help_queue) > 0:
                print(msg[1]['message'])
                q = help_queue.pop(0)
                print(q.getName() + ' is poppad')
                att_message = {"attending": True, "message":msg[1]['message']}
                att_messageJSON = json.dumps(att_message)
                for ide in q.getID():
                    send_service([ide, bytes(att_messageJSON, 'UTF-8')])
                del q
                gui.update_queue(help_queue)
                send_queue()
                msg = None

# # source venv/bin/activate
