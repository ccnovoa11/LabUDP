package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interface extends JFrame implements ActionListener{

    private JLabel ServerIP;
    private JLabel ServerPort;
    private JLabel BufferSize;
    private JLabel ObjectsNumber;
    private JLabel Clientid;

    private JTextField ServerIPField;
    private JTextField ServerPortField;
    private JTextField BufferSizeField;
    private JTextField ObjectsNumberField;
    private JTextField ClientidField;

    private JButton accept;

    public static final int weidht = 250;
    public static final int height = 300;
    public static final int rowsNumber = 10;

    private Client client;

    public Interface(){

    setTitle("Enter all Information");
    setSize(weidht,height);
    setVisible(true);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new GridLayout(rowsNumber,2));

    JPanel panel1 = new JPanel();
    panel1.setPreferredSize(new Dimension(weidht, height/rowsNumber));
    panel1.setLayout(new GridLayout(1,1));
    ServerIP=new JLabel("Ip Server:", SwingConstants.CENTER);
    panel1.add(ServerIP);
    ServerIPField= new JTextField("localhost",SwingConstants.HORIZONTAL);
    panel1.add(ServerIPField);
    add(panel1);

    JPanel panel2 = new JPanel();
    panel2.setPreferredSize(new Dimension(weidht, height/rowsNumber));
    panel2.setLayout(new GridLayout(1,2));
    ServerPort=new JLabel("Server Port:", SwingConstants.CENTER);
    panel2.add(ServerPort);
    ServerPortField= new JTextField("65000",SwingConstants.HORIZONTAL);
    panel2.add(ServerPortField);
    add(panel2);

    JPanel panel3 = new JPanel();
    panel3.setPreferredSize(new Dimension(weidht, height/rowsNumber));
    panel3.setLayout(new GridLayout(1,3));
    BufferSize=new JLabel("Buffer Size:", SwingConstants.CENTER);
    panel3.add(BufferSize);
    BufferSizeField= new JTextField("1000",SwingConstants.HORIZONTAL);
    panel3.add(BufferSizeField);
    add(panel3);

    JPanel panel4 = new JPanel();
    panel4.setPreferredSize(new Dimension(weidht, height/rowsNumber));
    panel4.setLayout(new GridLayout(1,4));
    ObjectsNumber=new JLabel("Objects Number:", SwingConstants.CENTER);
    panel4.add(ObjectsNumber);
    ObjectsNumberField= new JTextField("10000",SwingConstants.HORIZONTAL);
    panel4.add(ObjectsNumberField);
    add(panel4);

    JPanel panel6=new JPanel();
    panel6.setPreferredSize(new Dimension(weidht, height/rowsNumber));
    panel6.setLayout(new GridLayout(1,6));
    add(panel6);

    accept = new JButton("Enter");
    accept.setActionCommand("c1");
    accept.addActionListener(this);
    add(accept);

    validate();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    String command= e.getActionCommand();
        if(command.equalsIgnoreCase(accept.getActionCommand())){
            start();
        }
    }

    public void start(){

        if(isNumeric(ServerPortField.getText())&&isNumeric(BufferSizeField.getText())
            && isNumeric(ObjectsNumberField.getText())){

            client= new Client(ServerIPField.getText(),
                    Integer.parseInt(ServerPortField.getText()),Integer.parseInt(BufferSizeField.getText()),
                    Integer.parseInt(ObjectsNumberField.getText()));
            client.start();
            this.dispose();
        }
        else{
            JOptionPane.showMessageDialog(null,"the data has been entered in an incorrect format or this is not complete","Error",JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean isNumeric(String string){
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public static void main(String[] args){
        new Interface();
    }
}
