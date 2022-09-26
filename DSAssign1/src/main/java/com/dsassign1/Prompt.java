package com.dsassign1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class Prompt extends GUI{

    private JFrame frame = new JFrame("Client");

    private JButton studentView = new JButton();
    private JButton supervisorView = new JButton();

    private JTextField connectionId = new JTextField(20);

    private JLabel ipLable = new JLabel();

    private boolean connectRequest = false;

    public Prompt(){
    }

    public void init(){
        frame.setSize(400,100);
        frame.setLayout(new GridLayout());
        frame.setVisible(true);

        studentView.setText("Connect");

        studentView.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt) {
                System.out.printf("AHEtHEA");
                StudentView StudentV = new StudentView();
                StudentV.init();
                frame.dispose();

            }
        });

        supervisorView.setText("Supervisor");
        supervisorView.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                SupervisorView SupervisorV = new SupervisorView();
            }
        });
        ipLable.setText("Connect to tcp connection: ");
        frame.add(ipLable);
        frame.add(connectionId);



        frame.add(studentView);
        //frame.add(supervisorView);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public String getConnectionId(){
        return connectionId.getText().toString();
    }

    public boolean getConnectionRequest(){
        return connectRequest;
    }





}
