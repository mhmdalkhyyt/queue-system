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

    static ClientLogic cl;

    public static void main(String[] args) throws Exception
    {

        gui.init();

            if(args.length > 0){

                cl = new ClientLogic(args);
                cl.run();
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

};

