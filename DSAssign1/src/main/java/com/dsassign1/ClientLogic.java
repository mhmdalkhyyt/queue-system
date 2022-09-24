package com.dsassign1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;



public class ClientLogic {
    private String[] args;
    private GUI gui;
    private String queueStringList = "";


    public ClientLogic(String[] argStr, GUI window){
        this.args = argStr;
        this.gui = window;

    }

    public void run() throws InterruptedException{

        //For some reason ZMQ doesn't wait unless busy wait with thread.sleep

        while(!gui.isEnterQueue()){
            //wait for request to stand in queue
            Thread.sleep(1);
        }

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect(args[0]);

        while (gui.isEnterQueue()){

                String subscribe = "{\"subscribe\": true }";
                socket.send(subscribe);


                String payload = "{\"enterQueue\": true,\n" + " \"name\": \"" + gui.getNameTextField().getText() + "\"}";

                socket.send(payload);

                byte[] response = socket.recv();
                //System.out.println("Reply from server: " + new String(response, ZMQ.CHARSET));

                handleJSON(new String(response, ZMQ.CHARSET));

                while(true){
                    socket.send("{}");
                    Thread.sleep(4000);
                    System.out.println("4 secs passed, sending heartbeat");
                    gui.setQueueArea("");
                    socket.send(subscribe);
                    byte[] subResponse = socket.recv();
                    handleJSON(new String(subResponse, ZMQ.CHARSET));

                }
            }
    }
    public void handleJSON(String str){
        JSONObject jsonObject = new JSONObject(str);
        JSONArray jsArr;

        if(jsonObject.has("queue")) {
            jsArr = jsonObject.getJSONArray("queue");

            System.out.println("Queue List is :" + jsArr);
            for(int i =0; i < jsArr.length(); i++){
                if(i <= jsArr.length() -1){
                    queueStringList += (i+1) + " : " + jsArr.getJSONObject(i).getString("name") + "\n";
                }
                else if(i > jsArr.length()-1){
                    queueStringList += (i++) + " : " + jsArr.getJSONObject(i +1).getString("name") + " <--- You";
                }

            }

            //queueStringList += jsArr.getJSONObject(jsArr.length() -1).getString("name") + " <--- YOU";
            gui.setQueueArea(queueStringList);
        }

    }

}
