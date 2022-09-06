import tkinter

import zmq
from time import sleep
from tkinter import *


class GUI():

    def __init__(self):
        self.window = Tk()
        self.window.title("Queue client")
        self.window.geometry('500x800')
        self.label = tkinter.Label(self.window, text='Enter your name:', font=('Arial', 18))
        self.label.pack(padx=20, pady=20)

        self.name_box = tkinter.Text(self.window, height=1)
        self.name_box.pack(padx=20)

        self.send_button = tkinter.Button(self.window, text='Get in the queue', font=('Arial', 18),
                                     command=self.queue_button)
        self.send_button.pack(padx=20, pady=20)

        self.queue_box = tkinter.Text(self.window, font=('Arial', 16))
        self.queue_box.pack(padx=20)

        self.window.mainloop()

    def show_dict(self, d):
        print('Inside show dict')
        # for k, v in d.items():
        #     print(d[k], d[v])
        #     self.queuebox.insert(tkinter.END, "key = {}, val = {}\n".format(k, v))

    def queue_button(self):
        name = self.name_box.get('1.0', tkinter.END)
        print(name)
        print('in button')
        socket.send_json({'enterQueue': True, 'name': name})
        # queue_msg = socket.recv_json()
        #print(queue_msg)


context = zmq.Context()
socket = context.socket(zmq.DEALER)

gui = GUI()
socket.connect('tcp://tinyqueue.cognitionreversed.com:5556')
socket.send_json({'subscribe': True})
queue_msg = socket.recv_json()
print(queue_msg)
gui.show_dict(queue_msg)


while True:
    message = socket.recv_json()
    gui.show_dict(message)

    if 'ticket' in message:
        print(message, sep='\n')

    elif 'queue' in message:
        # print(*message, sep='\n')
        # print(message['name'])
        print(type(message))
        gui.show_dict(message)
        for dicts in message:
            for key, value in message.items():
                print(key, ' : ', value)

    else:
        socket.send_json('')

#
#
# # source venv/bin/activate
# def print_queue(queue_msg):
#     print("Printing queue")
#     for i in queue_msg:
#         print(i)
#
#

#
#
# message = socket.recv_json()
# print(message)

#
