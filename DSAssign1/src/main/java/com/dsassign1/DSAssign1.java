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

import com.fasterxml.jackson.core;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DSAssign1
{
    public static void main(String[] args) throws Exception
    {
        
        if(args.length > 0){
            try (ZContext context = new ZContext()) {
                Socket socket = context.createSocket(SocketType.DEALER);
                socket.connect(args[0]);
                
                
                String name = "Mometo";
                ObjectMapper mapper = new ObjectMapper();
                String payload = "{\"enterQueue\": true,\n" + " \"name\": \"Muhammed\"}";
                
                
                socket.send(payload);
                
                byte[] response = socket.recv();
                System.out.println("Reply from server: " + new String(response, ZMQ.CHARSET));
                
                
            }
        }
        else{
            System.out.println("Please specify URL");
        
        }
    }
}