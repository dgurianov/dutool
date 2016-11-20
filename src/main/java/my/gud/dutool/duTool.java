/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.gud.dutool;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Desktop Usage Tool Used to - format pasted text into JiraTable format -
 * format pasted text as Oracle SQL in(...) fragment
 *
 * @author Danylo
 */
public class duTool implements ActionListener {

    //Global window components 
    private JPanel panelMain;
    private JPanel panelBottom;
    private JFrame frameMain;
    private JScrollPane scrollPaneTextAreaMain;

    //Text  areas 
    private JTextArea textAreaMain;
    private JTextField textFieldDelimiter;

    //Buttons
    private JButton buttonJiraTable;
    private JButton buttonOracleInClause;

    public duTool() {
        frameMain = new JFrame("DUTool");
        panelMain = new JPanel();
        panelMain.setLayout(new BorderLayout());
        panelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        frameMain.getContentPane().add(panelMain,BorderLayout.CENTER);
                
        //Main text input  window 
        textAreaMain = new JTextArea(10, 10);
        
        //Set scrolling pane 
        scrollPaneTextAreaMain = new JScrollPane(textAreaMain);
        
        //Fill in with widgets
        addWidgets();
        
        frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameMain.setSize(800, 400);
        frameMain.setVisible(true);
        
        
    }
    
    
    
    //Fill in  main frame with all widgets
    private void addWidgets() {
                              
        panelMain.add(scrollPaneTextAreaMain, BorderLayout.CENTER);
        panelMain.add(panelBottom,BorderLayout.SOUTH);
        
        buttonJiraTable = new JButton("Jira Table");
        buttonJiraTable.addActionListener(this);
        panelBottom.add(buttonJiraTable);
        
        textFieldDelimiter = new JTextField(",",1);
        panelBottom.add(textFieldDelimiter);
        
        
        buttonOracleInClause = new JButton("Oracle IN");
        buttonOracleInClause.addActionListener(this);
        
        panelBottom.add(buttonOracleInClause);
        
             

    }

    public static void main(String[] args) {

        duTool dt = new duTool();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (null != e.getActionCommand() && "Jira Table".equals(e.getActionCommand())){
            formatAsJiraTable(textAreaMain);
        }else if    (null != e.getActionCommand() && "Oracle IN".equals(e.getActionCommand())) {
            formatAsOracleInClause(textAreaMain);
        }else {  
                textAreaMain.setText("Got not implemented command!"+e.getActionCommand()
                );
        }
            
        
    }
    
    private void formatAsJiraTable(JTextArea textAreaMain){
        String[] textBlockArray = textAreaMain.getText().split("\n");
        textBlockArray[0] = "||"+textBlockArray[0]+"||";
        textBlockArray[0] = textBlockArray[0].replace(textFieldDelimiter.getText(), "||");
        for (int i = 1; i < textBlockArray.length ; i++){
            textBlockArray[i] = "|"+textBlockArray[i]+"|";
            textBlockArray[i] = textBlockArray[i].replace(textFieldDelimiter.getText(), "|");
        }
        textAreaMain.setText(String.join("\n", textBlockArray));
        
    }

    private void formatAsOracleInClause(JTextArea textAreaMain){
        String[] textBlockArray = textAreaMain.getText().split("\n");
        Pattern findFirstWord = Pattern.compile("\\b[a-zA-Z0-9]+\\b");
        for (int i = 0 ; i < textBlockArray.length ; i++) {
            Matcher matchWord = findFirstWord.matcher(textBlockArray[i]);
            if (matchWord.find()){
                textBlockArray[i] = "'"+matchWord.group()+"'";
            }else {
                textBlockArray[i] = null ;
            } 
        }
        StringBuilder formattedOutput = new StringBuilder();
        for (String element : textBlockArray){
            if (element != null && !element.trim().isEmpty()){
                formattedOutput.append(element);
                formattedOutput.append(",");
            }
        }
        if (formattedOutput.length() > 0){
            formattedOutput.deleteCharAt(formattedOutput.length()-1);
            formattedOutput.append(")");
            formattedOutput.insert(0, "in (");
        }else {
            
          textAreaMain.setText("No words found in text!!!\n");
        }   
        textAreaMain.setText(formattedOutput.substring(0));
    }
}
