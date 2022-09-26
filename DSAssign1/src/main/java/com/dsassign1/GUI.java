/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.dsassign1;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 *
 * @author muham
 */
public class GUI {
    String view = "Student Client";


    private JFrame TopLevelFrame = new JFrame(view);
    private JFrame queueAreaFrame = new JFrame();

    private JLabel clientName = new JLabel();
    private JLabel nameLable = new JLabel();
    private JTextField nameTextField = new JTextField();
    private JButton btnEnterQueue = new JButton();

    private JList<String> queueList;

    private JList<String> supervisorList;

    private ArrayList<String> studentArr = new ArrayList<>();

    private DefaultListModel<String> studentDlm = new DefaultListModel<>();
    private DefaultListModel<String> supervisorDlm = new DefaultListModel<>();

    private ArrayList<String> supervisors = new ArrayList<>();
    //private JScrollPane queueScroll;


    private boolean enterQueue = false;

    public GUI(){
        init();
    }

    public void init(){
        TopLevelFrame.setSize(400,100);
        TopLevelFrame.setLayout(new GridLayout(2,1));
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
        for(int i =0; i < str.size(); i++){
            studentDlm.addElement(str.get(i));
        }
    }

    public void createSupervisorList(ArrayList<String> str){
        for(int i =0; i < str.size(); i++){
            supervisorDlm.addElement(str.get(i));
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

        //clientName.setText("Student");
        //panel.add(clientName, BorderLayout.NORTH);
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
        //panel.setBorder(new TitledBorder("Queue"));
        panel.setLayout(new FlowLayout());

        //JScrollPane listScroller = new JScrollPane(queueList);
       // listScroller.setPreferredSize(new Dimension(250,90));
        panel.setToolTipText("Students Queue Area");

        queueList = new JList<>(getDlm());

        queueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        queueList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        queueList.setVisibleRowCount(-1);

        //panel.add(listScroller);
        panel.add(queueList);
        return panel;
    }

    public JPanel createSupervisorArea(ArrayList<String> strArr){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        supervisorList = new JList<>();
        supervisorList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        supervisorList.setVisibleRowCount(-1);

        panel.add(supervisorList);

        return panel;
    }


    public DefaultListModel<String> getDlm(){
        return studentDlm;
    }

    public JPanel attendNotifier(boolean beingNotified){
        JPanel panel = new JPanel();

        if(beingNotified){

            panel.setLayout(new FlowLayout());
            JLabel label = new JLabel();
            label.setText("Its your turn to be attended!");


            panel.add(label);


        }
        return panel;

    }

    public void getStudentList(ArrayList<String> stringArrayList){
        studentArr = stringArrayList;
    }
}


