package com.dsassign1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;


import java.util.ArrayList;
import java.util.Objects;


//Todo all servers disconnected



public class ClientLogic {




    private String[] args;

    private GUI gui;

    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> supervisors = new ArrayList<>();

    private int servers;

    private ArrayList<String> serverConnected = new ArrayList<>();

    public ClientLogic(String[] argStr, GUI gui){
        this.args = argStr;
        this.gui = gui;
    }

    public void run() throws InterruptedException{

        //For some reason ZMQ doesn't wait unless busy wait with thread.sleep

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);

        System.out.println(":::INITIALIZING:::");

        for(int i= 0; i < args.length; i++){
            socket.connect(args[i]);
            String subscribe = "{\"subscribe\": true}";
            System.out.println("Sending subscribe message to : " + args[i] + " message sent : " + subscribe);
            socket.send(subscribe);
            serverConnected.add(args[i]);

            servers++;

            System.out.println("Servers connected : " + servers);
        }



        while(!gui.isEnterQueue()){
            //wait for request to stand in queue
            Thread.sleep(1);
        }

        enterQueueRequest(socket);
        handleJSON(socket);
        //sendheartbeat(socket);
        //handleJSON(socket);

    }

    public ArrayList<String> getStudents(){
        return students;
    }




    public void handleJSON(ZMQ.Socket socket){

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    byte[] str = socket.recv();
                    System.out.println("Received server response");
                    JSONObject jsonObject = new JSONObject(new String(str, ZMQ.CHARSET));
                    JSONArray studentjsArr;
                    JSONArray supervisorjsArr;
                    System.out.println(jsonObject);
                    String receivId = null;

                    if(jsonObject.has("serverId")){
                        receivId = jsonObject.getString("serverId").toString();
                        System.out.printf("Hello im: : : : " + receivId + "\n");
                    }


                    if(jsonObject.has("queue")) {
                        studentjsArr = jsonObject.getJSONArray("queue");

                        for(int i =0; i < studentjsArr.length(); i++){
                            students.add(studentjsArr.getJSONObject(i).getString("name"));
                            //System.out.println(students.get(i).toString());
                        }

                        gui.setQueueArea(students);
                        students.clear();
                    }
                    else if (jsonObject.has("supervisors")){
                        supervisorjsArr = jsonObject.getJSONArray("supervisors");

                        for(int i= 0; i< supervisorjsArr.length(); i++){
                            supervisors.add(supervisorjsArr.getJSONObject(i).getString("name"));
                            //System.out.println("Supervisors online : " + supervisors.get(i).toString());
                        }
                        gui.createSupervisorList(supervisors);
                        supervisors.clear();
                    }
                    else if (jsonObject.has("attending")){
                        String msg = jsonObject.getString("message");

                        //System.out.println(msg);
                        gui.attendNotifier(true, msg);

                    }
                    else if(jsonObject.has("serverId")){
                        System.out.printf("Server sent you a heartbeat, RESPOND!");
                        for(int i = 0; i < serverConnected.size(); i++){
                            String heartbeatmsg = "{}";
                            socket.send(heartbeatmsg);
                            System.out.println("Sent heartbeat to server ----> " + serverConnected.get(i));

                        }


                    }

                }

            }
        });
        thread1.start();

    }


    public void enterQueueRequest(ZMQ.Socket socket){
        String payload = "{\"enterQueue\": \"true\"," + " \"name\": \"" + gui.getNameTextField().getText() + "\"}";
        System.out.println(payload);

        for(int i =0; i <= servers; i++){
            socket.send(payload);
        }
    }

    public void sendheartbeat(ZMQ.Socket socket){
        Thread thisThread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(4000);

                } catch (InterruptedException e) {
                    //throw new RuntimeException(e);
                }

            }
        });
        thisThread.start();

    }



}
