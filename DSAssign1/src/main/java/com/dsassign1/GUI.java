/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.dsassign1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author muham
 */
public class GUI {
    String view = "Student Client";

    private JFrame frame = new JFrame(view);
    private JLabel nameLable = new JLabel();
    private JTextField nameTextField = new JTextField();
    private JButton btnEnterQueue = new JButton();
    private JTextArea queueArea = new JTextArea();


    private boolean enterQueue = false;

    public void init(){
        frame.setSize(400,500);
        frame.setVisible(true);
        frame.setLayout(new FlowLayout());

        //nameLable
        nameLable.setText("Name: ");
        frame.add(nameLable);

        //nameTextField
        nameTextField.setColumns(20);
        frame.add(nameTextField);

        //btnEnterQueue
        btnEnterQueue.setText("Enter Queue");
        btnEnterQueue.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnEnterQueueActionPerformed(evt);
                System.out.println("Your name is: " + getNameTextField().getText());

            }
        });
        frame.add(btnEnterQueue);


        frame.add(queueArea);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public JTextField getNameTextField() {
        return nameTextField;
    }

    public void setNameTextField(String str) {
        this.nameTextField.setText(str);
    }

    public JTextArea getQueueArea() {
        return queueArea;
    }

    public void setQueueArea(String str) {
        this.queueArea.append(str);
    }
    private void btnEnterQueueActionPerformed(ActionEvent evt){
        enterQueue = true;
    }
    public boolean isEnterQueue() {
        return enterQueue;
    }


    public void queueAreaActionPerformed(ActionEvent evt){
        String text = queueArea.getText();
        queueArea.append(text);
    }
}


