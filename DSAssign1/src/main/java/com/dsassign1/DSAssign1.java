/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.dsassign1;

/**
 *
 * @author muham
 */

import org.zeromq.ZMQ;
import org.zeromq.*;
import org.zeromq.ZContext;
import org.zeromq.ZMQ.Socket;
import org.json.*;

import javax.swing.text.html.HTMLDocument;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class DSAssign1
{
    static ArrayList<String> studentList = new ArrayList<>();
    static JSONArray jsArr;
    static GUI gui = new GUI();

    public static void main(String[] args) throws Exception
    {

        gui.init();

        Thread.sleep(10000);

            if(args.length > 0){
                try (ZContext context = new ZContext()) {
                    Socket socket = context.createSocket(SocketType.DEALER);
                    socket.connect(args[0]);


                    String subscribe = "{\"subscribe\": true }";
                    socket.send(subscribe);
                    byte[] subResponse = socket.recv();

                    handleJSON(new String(subResponse, ZMQ.CHARSET));
                    gui.setQueueArea(new String(subResponse, ZMQ.CHARSET));


                    String payload = "{\"enterQueue\": true,\n" + " \"name\": \"" + gui.getNameTextField().getText() + "\"}";
                    Person student = new Person("0", gui.getNameTextField().getText());

                    socket.send(payload);

                    byte[] response = socket.recv();
                    System.out.println("Reply from server: " + new String(response, ZMQ.CHARSET));


                    JSONObject jsonObject = new JSONObject();

                    //jsonObject.getJSONObject(new String(subResponse1, ZMQ.CHARSET));


                    while(true){
                        socket.send("{}");
                        Thread.sleep(4000);
                        System.out.println("4 secs passed, sending heartbeat");

                    }

                }

            }
            else{
                System.out.println("Please specify URL");

            }
        }


    public static void sendHeartbeat(Socket socket){
        System.out.println("Trying to send heartbeat");
        Runnable heartbeat = () -> {

                String h = "{}";
                socket.send(h);

                System.out.println("helloo heartbeat");


        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(heartbeat, 0, 3, TimeUnit.SECONDS);
        }
    public static void handleJSON(String str){

        JSONObject jsonObject = new JSONObject(str);
        JSONArray key = jsonObject.names();
        key.iterator();
        for(int i = 0 ; i < key.length(); i++){
            if(key.getString(i) == "\"queue\""){
                //JSONArray valuekey = key.getJSONArray(i);
                JSONArray keyvalue = jsonObject.getJSONObject(key.getString(i)).names();
                System.out.println("There was a queue message");
                for(int j = 0; j < keyvalue.length(); j++){
                    System.out.println("There was a key in the que");
                }
            }

        }
        /*
        if(jsonObject.has("queue")) {
            jsArr = jsonObject.getJSONArray("queue");
            String qString = "";
            for(int i =0; i<jsArr.length(); i++){
                qString += (i + 1) + " : " + jsArr.getJSONObject(i).getString("name") + "\n";
                //System.out.println("Names in queue" + jsArr.getJSONObject(i).getString("name") + "\n");

            }
           gui.getQueueArea(qString);
        }*/

    }
};

