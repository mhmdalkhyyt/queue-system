/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */


package com.dsassign1;

/**
 *
 * @author muham
 */



public class DSAssign1
{
    public static void main(String[] args) throws Exception {
      GUI gui = new GUI();

       ClientLogic cLogic;

           if(args.length > 0){

                cLogic = new ClientLogic(args, gui);
                cLogic.run();
            }
            else{
                System.out.println("Please specify URL");

            }

    }

};

