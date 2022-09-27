import tkinter
from time import sleep
import time
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

        for x in range(len(arg) - 1):
            client.socket.send_json({'enterQueue': True, 'name': name})
        heartb.startHeartBeats()

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


        self.root.mainloop()


class FLClient(object):
    def __init__(self):
        self.servers = 0
        self.sequence = 0
        self.context = zmq.Context()
        self.socket = self.context.socket(zmq.DEALER)  # DEALER
        self.monitor_socket = self.socket.get_monitor_socket()

    def connect(self, endpoint):
        self.socket.connect(endpoint)
        self.servers += 1
        serverlist.append([endpoint[16:], time.time(), True])
        print("I: Connected to %s" % endpoint)
        self.socket.send_json({'subscribe': True})


class HeartBeat():

    def __init__(self):
        threading.Thread.__init__(self)
        self.heartThreadQ = threading.Thread(target=self.heartbeatQ)

    def heartbeatQ(self):
        sendMsg = True

        while True:
            aliveservers = len(serverlist)
            for x in serverlist:
                if bool(x[2]):  # the server status is True
                    if x[1] < (time.time() - 30.0):
                        print('Server ' + x[0] + ' has disconnected')
                        x[2] = False  # server status sets to False
                        aliveservers -= 1

                else:
                    aliveservers -= 1
                if aliveservers == 0 and sendMsg:
                    gui.show_msg('All servers has disconnected')
                    sendMsg = False
            sleep(2)

    def startHeartBeats(self):

        self.heartThreadQ.start()




gui = GUI()
sleep(1)
print('gui complete')
heartb = HeartBeat()
queuelist = list()
serverlist = list()
client = FLClient()
arg = sys.argv
for endpoint in sys.argv[1:]:
    client.connect('tcp://127.0.0.1:' + str(endpoint))

# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
# socket.connect('tcp://127.0.0.1:' + arg[1])
# ------------------------------------------

while True:
    message = client.socket.recv_json()
    server_id = message['serverId']
    for server in serverlist:  # updates time since last connection and sets server status to True
        if server[0] == str(server_id):
            server[1] = time.time()
            if server[1] == False:
                server[2] = True
                gui.queue_button()
    print(serverlist)
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

    else:
        for x in range(len(arg) - 1):
            client.socket.send_json("{}")

# # source venv/bin/activate
