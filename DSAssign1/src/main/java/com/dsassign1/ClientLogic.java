package com.dsassign1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;


import java.util.ArrayList;

//Todo Server doesnt display subscribed queue as soon as connected.
//Todo all servers disconnected.
//Todo In case a client assigns to queue, and you want to put another student i queue, you have to restart client.
//Todo send nameMsg to gui to show who supervisor is attending




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
            //System.out.println("Sending subscribe message to : " + args[i] + " message sent : " + subscribe);
            socket.send(subscribe);
            serverConnected.add(args[i]);

            servers++;

            //System.out.println("Servers connected : " + servers);
        }
        handleJSON(socket);


        while(!gui.isEnterQueue()){
            //wait for request to stand in queue
            Thread.sleep(1);

        }

        enterQueueRequest(socket);
        handleJSON(socket);


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

                        }

                        gui.setQueueArea(students);
                        students.clear();
                    }
                    else if (jsonObject.has("supervisors")){
                        supervisors.clear();
                        supervisorjsArr = jsonObject.getJSONArray("supervisors");

                        for(int i= 0; i< supervisorjsArr.length(); i++){
                            supervisors.add(supervisorjsArr.getJSONObject(i).getString("name"));
                        }
                        gui.updateSupervisorList(supervisors);

                    }
                    else if (jsonObject.has("attending")){
                        String msg = jsonObject.getString("message");
                        String supervisor = jsonObject.getString("supervisor");
                        String nameMsg = jsonObject.getString("name");
                        ArrayList<String> newList = supervisors;


                        //send nameMsg to gui to show who the supervisor is attending

                        for(int i = 0; i < supervisors.size(); i++){
                            if(supervisor.equals(supervisors.get(i))){
                                System.err.println("DEN KOMMER IN HÄR");

                                newList.set(i, supervisor + " is attending " + nameMsg);
                                System.err.println("Supervisors array contains : " + supervisors.get(i));

                            }
                        }
                        gui.updateSupervisorList(newList);

                        //System.out.println(msg);
                        gui.attendNotifier(true, msg, supervisor, nameMsg);

                    }
                    else if(jsonObject.has("serverId")){
                        System.out.printf("Server sent you a heartbeat, RESPOND!");
                        for(int i = 0; i < serverConnected.size(); i++){
                            String heartbeatmsg = "{}";
                            socket.send(heartbeatmsg);
                            System.out.println("Sent heartbea  t to server ----> " + serverConnected.get(i));

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





}
