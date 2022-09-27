import tkinter
from time import sleep
import time
import zmq
import zmq.utils.monitor
import sys
import os
from tkinter import *
import threading

GLOBAL_TIMEOUT = 2500


class GUI(threading.Thread):

    def __init__(self):
        threading.Thread.__init__(self)
        self.su_box = None
        self.queue_box = None
        self.s_button = None
        self.a_button = None
        self.name_box = None
        self.msgbox = None
        self.first_label = None
        self.second_label = None
        self.root = None
        self.button_frame = None
        self.start()
        self.user_name = None

    def callback(self):
        os._exit(0)

    def update_queue(self, d):

        if self.queue_box is not None:
            self.queue_box.delete(1.0, END)
        self.user_name = self.name_box.get(1.0, tkinter.END)
        self.user_name = self.user_name[:-1]
        i = 1
        for x in d:
            if x == self.user_name:
                self.queue_box.insert(END, str(i) + ': ' + x + '           <----- You' + '\n')
            else:
                self.queue_box.insert(END, str(i) + ': ' + x + '\n')
            i = i + 1

    def update_supervisors(self, d):
        if self.su_box is not None:
            self.su_box.delete(1.0, END)
        self.user_name = self.name_box.get(1.0, tkinter.END)
        self.user_name = self.user_name[:-1]
        i = 1
        for x in d:
            if x == self.user_name:
                self.su_box.insert(END, str(i) + ': ' + x + '           <----- You' + '\n')
            else:
                self.su_box.insert(END, str(i) + ': ' + x + '\n')
            i = i + 1

    def supervise_button_m(self):
        name = self.name_box.get(1.0, tkinter.END)
        name = name[:-1]
        for x in range (len(arg)-1):
            client.socket.send_json({'supervisor': True, 'name': name})
        heart_thread = threading.Thread(target=heartbeat)
        heart_thread.start()

    def attend_button(self):

        msg = self.msgbox.get(1.0, tkinter.END)
        msg = msg[:-1]
        next_student = str(queuelist[0])
        print('next is ' + next_student)
        client.socket.send_json({"attend": True, "name": next_student, "message": msg})

    def run(self):
        self.root = Tk()
        self.root.protocol("WM_DELETE_WINDOW", self.callback)
        self.root.title("Supervisor client")
        self.root.geometry('500x800')
        self.first_label = tkinter.Label(self.root, text='Enter your name:', font=('Arial', 18))
        self.first_label.pack(padx=20, pady=5)

        self.name_box = tkinter.Text(self.root, height=1, width=15)
        self.name_box.pack(padx=20)
        self.button_frame = tkinter.Frame(self.root)
        self.s_button = tkinter.Button(self.button_frame, text='Supervise', font=('Arial', 16),
                                       command=self.supervise_button_m)
        self.s_button.grid(row=0, column=0, padx=5)
        self.a_button = tkinter.Button(self.button_frame, text='Attend', font=('Arial', 16), command=self.attend_button)
        self.a_button.grid(row=0, column=1, padx=5)
        self.msgbox = tkinter.Text(self.button_frame, height=1, width=20)
        self.msgbox.grid(row=0, column=2, padx=5)

        self.button_frame.pack(pady=10)

        self.queue_box = tkinter.Text(self.root, height=18, font=('Arial', 16))
        self.queue_box.pack(padx=20, pady=10)

        self.second_label = tkinter.Label(self.root, text='Supervisors:', font=('Arial', 18))
        self.second_label.pack(padx=20)

        self.su_box = tkinter.Text(self.root, height=4, font=('Arial', 16))
        self.su_box.pack(padx=20, pady=20)
        # socket.send_json({'subscribe': True})
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
        print("I: Connected to %s" % endpoint)
        self.socket.send_json({'subscribe': True})

    # def monitor_worker(self):
    #     monitor_socket = self.socket.get_monitor_socket()
    #     monitor_socket.linger = 0
    #     poller = zmq.Poller()
    #     poller.register(monitor_socket, zmq.POLLIN)
    #     while self.enabled:
    #         socks = poller.poll()
    #         if len(socks) > 0:
    #             data = recv_monitor_message(monitor_socket)
    #             event = data['event']
    #             if event == zmq.EVENT_CONNECTED:
    #                 # logger.warning('Connected to {0}'.format(self.zmq_socket_url))
    #                 print('Event: connected')
    #             elif event == zmq.EVENT_DISCONNECTED:
    #                 # logger.warning('Connection to {0} was disconencted'.format(self.zmq_socket_url))
    #                 print('Event: disconnected')

    def sendMsg(self, *request):
        pass
        # # Prefix request with sequence number and empty envelope
        # self.sequence += 1
        # msg = ['', str(self.sequence)] + list(request)
        #
        # # Blast the request to all connected servers
        # for server in xrange(self.servers):
        #     # self.socket.send_multipart(msg)
        #     pass
        #
        # # Wait for a matching reply to arrive from anywhere
        # # Since we can poll several times, calculate each one
        # poll = zmq.Poller()
        # poll.register(self.socket, zmq.POLLIN)
        #
        # reply = None
        # while time.time() < endtime:
        #     socks = dict(poll.poll())
        #     if socks.get(self.socket) == zmq.POLLIN:
        #         reply = self.socket.recv_multipart()
        #
        # return reply


gui = GUI()
sleep(1)
print('gui complete')

queuelist = list()

if len(sys.argv) == 1:
    print("I: Usage: %s <endpoint> ..." % sys.argv[0])
    sys.exit(0)

# Create new freelance client object
client = FLClient()

arg = sys.argv
for endpoint in sys.argv[1:]:
    client.connect('tcp://127.0.0.1:' + str(endpoint))

start = time.time()


def heartbeat():
    while (True):
        sleep(3)
        for x in range (len(arg)-1):
            client.socket.send_json('')


# arg = sys.argv
# for port in enumerate(arg, start=1):
#     print(port[1])
#     socket.connect('tcp://127.0.0.1:' + str(port[1]))
# ------------------------------------------
# Select the correct line for online or local communication
# ------------------------------------------
# socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
# socket.connect('tcp://127.0.0.1:7000')
# ------------------------------------------



while True:
    message = client.socket.recv_json()
    if 'ticket' in message:
        print('got ticket')
        print(message, sep='\n')
        #socket.send_json({"attend": True})

    elif 'queue' in message:
        print('got queue')
        print(message)
        x = [e["name"] for e in message['queue']]
        print(x)
        for name in x:
            if name not in queuelist:
                queuelist.append(name)
        gui.update_queue(queuelist)


    elif 'supervisors' in message:
        print('got supervisors')
        print(message)
        x = [e["name"] for e in message['supervisors']]
        gui.update_supervisors(x)

# # source venv/bin/activate
