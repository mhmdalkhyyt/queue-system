/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.dsassign1;

/**
 *
 * @author muham
 */
public class Person {
    
    
    private String name;
    private String ticketNr;
    
    public Person(String ticketNr, String name){
        this.name = name;
        this.ticketNr = ticketNr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicketNr() {
        return ticketNr;
    }

    public void setTicketNr(String ticketNr) {
        this.ticketNr = ticketNr;
    }
    
}
