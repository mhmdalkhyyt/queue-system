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
    public static void main(String[] args) throws Exception {
      GUI gui = new GUI();

       ClientLogic cLogic;

      // Prompt prompt = new Prompt();

      // prompt.init();


           if(args.length > 0){

                cLogic = new ClientLogic(args, gui);
                cLogic.run();
            }
            else{
                System.out.println("Please specify URL");

            }


    }

};

