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

        while(!gui.isEnterQueue()){
            //wait for request to stand in queue
            Thread.sleep(1);
        }

        ZContext context = new ZContext();
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect(args[0]);

        String subMsg = "{\"subscribe\": true\"}";
        socket.send(subMsg);

        byte[] subResp = socket.recv();
        handleJSON(new String(subResp, ZMQ.CHARSET));



        while (gui.isEnterQueue()){


                heartbeat(socket);


                String payload = "{\"enterQueue\": true,\n" + " \"name\": \"" + gui.getNameTextField().getText() + "\"}";

                socket.send(payload);

                byte[] response = socket.recv();
                //System.out.println("Reply from server: " + new String(response, ZMQ.CHARSET));

                handleJSON(new String(response, ZMQ.CHARSET));

                /*while(true){
                    socket.send("{}");
                    Thread.sleep(4000);
                    System.out.println("4 secs passed, sending heartbeat");
                    gui.getStudentList(students);

                }*/

            }
    }
    public void handleJSON(String str){
        JSONObject jsonObject = new JSONObject(str);
        JSONArray studentjsArr;
        JSONArray supervisorjsArr;


        if(jsonObject.has("queue")) {
            studentjsArr = jsonObject.getJSONArray("queue");

            //System.out.println("Queue List is :" + jsArr);
            for(int i =0; i < studentjsArr.length(); i++){
                if(i <= studentjsArr.length() -1){
                    queueStringList += (i+1) + " : " + studentjsArr.getJSONObject(i).getString("name") + "\n";
                    students.add(studentjsArr.getJSONObject(i).getString("name"));

                }
                else if(i > studentjsArr.length()-1){
                    queueStringList += (i++) + " : " + studentjsArr.getJSONObject(i +1).getString("name") + " <--- You";
                }

            }


            //queueStringList += jsArr.getJSONObject(jsArr.length() -1).getString("name") + " <--- YOU";
            gui.setQueueArea(students);
        }
        if(jsonObject.has("supervisors")){
            supervisorjsArr = jsonObject.getJSONArray("supervisors");
            for(int i= 0; i< supervisorjsArr.length(); i++){
                supervisors.add(supervisorjsArr.getJSONObject(i).getString("name"));
            }
            gui.createSupervisorList(supervisors);
        }


    }

    public ArrayList<String> getStudents(){
        return students;
    }

    public void heartbeat(ZMQ.Socket socket) throws InterruptedException {

        Thread.sleep(4000);
        socket.send("{}");


    }


}
