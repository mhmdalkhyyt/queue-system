package com.dsassign1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;


import java.util.ArrayList;
import java.util.Objects;




public class ClientLogic {

    class TestServer{
        long time;
        String myId;
        public TestServer(String ID){
            this.myId = ID;
            updateTime();
        }

        void updateTime(){
            this.time = System.currentTimeMillis()/1000;
        }

        boolean checkIfDead(){
            return ((System.currentTimeMillis() / 1000) - this.time) > (20);
        }
    }


    private String[] args;
    private Prompt prompt;
    private GUI gui;

    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> supervisors = new ArrayList<>();

    private int servers;

    private ArrayList<TestServer> serverList = new ArrayList<>();
    private ArrayList<Long> serverConnectedTime = new ArrayList<>();
    private TestServer currServer;


    public ClientLogic(String[] argStr, GUI gui){
        this.args = argStr;
        this.gui = gui;
    }

    public void run() throws InterruptedException{

        //For some reason ZMQ doesn't wait unless busy wait with thread.sleep

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);

        for(int i= 0; i < args.length; i++){
            socket.connect(args[i]);
            String subscribe = "{\"subscribe\": true}";
            System.out.println(subscribe);
            socket.send(subscribe);

            servers++;

            System.out.println("Servers connected : " + servers);
            System.out.println("Server connected to : ");

        }

        receiveMeth(socket, context);
        checkConnectivity();


        while(!gui.isEnterQueue()){
            //wait for request to stand in queue
            Thread.sleep(1);
        }

        enterQueueRequest(socket);


    }

    public ArrayList<String> getStudents(){
        return students;
    }

    public void checkConnectivity(){
        Thread thead2 = new Thread(new Runnable() {
            @Override
            public void run() {
               while(true){
                   boolean currSerDead = false;
                   int validAliveServer = -1;
                   for(int i = 0; i < serverList.size(); i++){
                        if(serverList.get(i).checkIfDead()){
                            //its dead
                            if(currServer == serverList.get(i)){
                                currSerDead = true;
                            }
                        }
                        else{
                            //is alive
                            validAliveServer = i;
                        }
                   }
                   if(currSerDead){
                       if(validAliveServer != -1) {
                           currServer = serverList.get(validAliveServer);
                           System.out.println("NEW CURR SERVER : " + currServer.myId);
                           gui.notifyServerDisconnected();

                       }
                       else{
                           //Skicka varningsmeddelande till användare
                           System.err.println("NO VALID ALIVE SERVERS!!!!");
                       }
                   }
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       //throw new RuntimeException(e);
                   }
               }

            }
        });
        thead2.start();
    }

    public void receiveMeth(ZMQ.Socket socket, ZContext ctx){

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

                    String receivId = jsonObject.getString("serverId");
                    createServer(receivId).updateTime();

                    if(currServer.myId.equals(receivId)){
                        if(jsonObject.has("queue")) {
                            studentjsArr = jsonObject.getJSONArray("queue");

                            for(int i =0; i < studentjsArr.length(); i++){
                                students.add(studentjsArr.getJSONObject(i).getString("name"));
                                System.out.println(students.get(i).toString());
                            }

                            gui.setQueueArea(students);
                            students.clear();
                        }
                        else if (jsonObject.has("supervisors")){
                            supervisorjsArr = jsonObject.getJSONArray("supervisors");

                            for(int i= 0; i< supervisorjsArr.length(); i++){
                                supervisors.add(supervisorjsArr.getJSONObject(i).getString("name"));
                                System.out.println("Supervisors online : " + supervisors.get(i).toString());
                            }
                            gui.createSupervisorList(supervisors);
                            supervisors.clear();
                        }


                    }
                    if (jsonObject.has("attending")){
                        String msg = jsonObject.getString("message");

                        System.out.println(msg);
                        gui.attendNotifier(true, msg);

                    }


                    String heartbeatmsg = "{}";
                    System.out.println("Sending heatbeat");
                    socket.send(heartbeatmsg);


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


    public TestServer createServer(String serverId){

        for(int i = 0; i < serverList.size(); i++){
            if(serverId.equals(serverList.get(i).myId)){

                return serverList.get(i);
            }
        }
        serverList.add(new TestServer(serverId));
        if(serverList.size() == 1){
            currServer = serverList.get(serverList.size()-1);
        }
        return serverList.get(serverList.size()-1);

    }


}
