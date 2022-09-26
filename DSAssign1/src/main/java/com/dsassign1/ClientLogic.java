package com.dsassign1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;


public class ClientLogic {
    private String[] args;
    private Prompt prompt;
    private GUI gui;
    private String queueStringList = "";

    private ArrayList<String> students = new ArrayList<>();
    private ArrayList<String> supervisors = new ArrayList<>();



    public ClientLogic(String[] argStr, GUI gui){
        this.args = argStr;
        this.gui = gui;
//        this.prompt = prompt;


    }

    public void run() throws InterruptedException{

        //For some reason ZMQ doesn't wait unless busy wait with thread.sleep

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        for(int i= 0; i < args.length; i++){
            socket.connect(args[0]);
        }



        receiveMeth(socket);

        String subscribe = "{\"subscribe\": true}";
        System.out.println(subscribe);
        socket.send(subscribe);



        while(!gui.isEnterQueue()){
            //wait for request to stand in queue
            Thread.sleep(1);
        }


        if (gui.isEnterQueue()) {


            String payload = "{\"enterQueue\": \"true\"," + " \"name\": \"" + gui.getNameTextField().getText() + "\"}";
            System.out.println(payload);

            socket.send(payload);
        }
    }

    public ArrayList<String> getStudents(){
        return students;
    }

    public void receiveMeth(ZMQ.Socket socket){

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


                    if(jsonObject.has("queue")) {
                        studentjsArr = jsonObject.getJSONArray("queue");

                        for(int i =0; i < studentjsArr.length(); i++){
                            students.add(studentjsArr.getJSONObject(i).getString("name"));
                            System.out.println(students.get(i).toString());
                        }


                        //queueStringList += jsArr.getJSONObject(jsArr.length() -1).getString("name") + " <--- YOU";
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
                    else if (jsonObject.has("attending")){
                        String msg = jsonObject.getString("message");

                        System.out.println(msg);

                        gui.attendNotifier(true, msg);
                    }

                    else {
                        System.out.println("Sending heatbeat");
                        socket.send("{}");
                    }

                }

            }

        });
        thread1.start();
    }


}
