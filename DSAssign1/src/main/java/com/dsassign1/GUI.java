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
import java.util.concurrent.Flow;

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
    private JTextArea txtArea = new JTextArea();

    private JTextArea supervisorTxtArea = new JTextArea();

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

            txtArea.append(str.get(i) + " \n");
        }
    }

    public void createSupervisorList(ArrayList<String> str){
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


        panel.add(supervisorTxtArea);

        return panel;
    }


    public DefaultListModel<String> getDlm(){
        return studentDlm;
    }

    public void attendNotifier(boolean beingNotified, String msg){
        //JFrame notifierFrame = new JFrame();
        //JPanel panel = new JPanel();


        if(beingNotified){
            JOptionPane.showMessageDialog(null, msg);

        }


    }

    public void getStudentList(ArrayList<String> stringArrayList){
        studentArr = stringArrayList;
    }

    public void notifyServerDisconnected(){
        JOptionPane.showMessageDialog(null, "NEW favorite SERVER");

    }
}


