/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.dsassign1;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Objects;

//Todo Gui should prompt all servers disconnected
//Todo Connect button to add multiple students

/**
 *
 * @author muham
 */
public class GUI {
    String view = "Student Client";


    private JFrame TopLevelFrame = new JFrame(view);



    private JLabel nameLable = new JLabel();
    private JTextField nameTextField = new JTextField();
    private JTextArea txtArea = new JTextArea();

    private JTextArea supervisorTxtArea = new JTextArea();

    private JButton btnEnterQueue = new JButton();

    private ArrayList<String> studentArr = new ArrayList<>();

    private ArrayList<String> supervisors = new ArrayList<>();



    private boolean enterQueue = false;

    public GUI(){
        init();
    }

    public void init(){
        TopLevelFrame.setSize(400,800);
        TopLevelFrame.setLayout(new GridLayout(3,2));
        TopLevelFrame.setVisible(true);


        TopLevelFrame.add(createStudentPromptPanel());
        TopLevelFrame.add(createQueueArea(studentArr));

        TopLevelFrame.add(createSupervisorArea(supervisors));


        TopLevelFrame.pack();

        TopLevelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public JTextField getNameTextField() {
        return nameTextField;
    }



    public void setQueueArea(ArrayList<String> str) {

        txtArea.setText("");

        for(int i =0; i < str.size(); i++){
            if(Objects.equals(str.get(i), nameTextField.getText())){
                txtArea.append( (i + 1) +  " : " + str.get(i) + "              <------ YOU" + "\n");
            }else{
                txtArea.append( (i +1 ) +  " : "  + str.get(i) + " \n");
            }

        }
    }

    public void updateSupervisorList(ArrayList<String> str){
        supervisorTxtArea.setText("");
        for(int i =0; i < str.size(); i++){
            supervisorTxtArea.append(str.get(i) + " \n");
        }
    }


    private void btnEnterQueueActionPerformed(ActionEvent evt){
        enterQueue = true;
    }


    public boolean isEnterQueue() {
        return enterQueue;
    }


    public JPanel createStudentPromptPanel(){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Student"));
        panel.setLayout(new FlowLayout());

        nameLable.setText("Name: ");

        panel.add(nameLable);
        nameTextField.setColumns(20);
        panel.add(nameTextField);

        btnEnterQueue.setText("Enter Queue");
        btnEnterQueue.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt) {
                btnEnterQueueActionPerformed(evt);
                System.out.println("Your name is: " + getNameTextField().getText());

            }
        });
        panel.add(btnEnterQueue);
        return panel;
    }

    public JPanel createQueueArea(ArrayList<String> strArr){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Queue Area"));
        panel.setLayout(new FlowLayout());

        panel.setToolTipText("Students Queue Area");

        panel.add(txtArea);
        return panel;
    }

    public JPanel createSupervisorArea(ArrayList<String> strArr){
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Supervisors"));
        panel.setLayout(new FlowLayout());

        panel.setToolTipText("Supervisor Area");


        panel.add(supervisorTxtArea);

        return panel;
    }



    public void attendNotifier(boolean beingNotified, String supervisor ,String msg , String studentName) {
        //JFrame notifierFrame = new JFrame();
        //JPanel panel = new JPanel();


        if (Objects.equals(nameTextField.getText(), studentName)) {
            if (beingNotified) {
                JOptionPane.showMessageDialog(null, msg);
            }

        }


    }
}


