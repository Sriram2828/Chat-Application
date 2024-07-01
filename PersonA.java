import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PersonA extends Frame implements Runnable, ActionListener{
    TextField textField;
    TextArea textArea;
    Button sendButton;

    ServerSocket serverSocket;
    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    Thread chatThread;

    // Constructor
    PersonA(){
        // creating the components for the ui
        textArea = new TextArea();
        textArea.setBounds(50, 50, 450, 450);
        textField = new TextField();
        textField.setBounds(50, 520, 200, 40);
        sendButton = new Button("send");
        sendButton.setBounds(260, 520, 100, 40);

        // actionlistener interface is used to create listener for the send button
        sendButton.addActionListener(this);

        try {
            // implementing the Server sockets for communication
            serverSocket = new ServerSocket(10000);
            socket = serverSocket.accept();

            // implementing the dataStreams for data transmission
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            // exception statement
        }
        

        // add all components to the fram
        add(textArea);
        add(textField);
        add(sendButton);

        // created thread for the listener 
        chatThread = new Thread(this);
        chatThread.setDaemon(true);
        chatThread.start();

        // userinterface
        setSize(600,600);
        setTitle("PersonA");
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        // generating the transmission data in the textfield
        String message = textField.getText();
        // display the generated text in the textArea
        textArea.append("PersonA: " + message+"\n");
        // empty the textfield after the message transmission occured
        textField.setText("");

        // send the genetated message
        try {
            // writeUTF() method is used to write the message into the outputStream
            dataOutputStream.writeUTF(message);
            // flush() method will send the message immediately, without storing it in the buffer
            dataOutputStream.flush();
        } catch (IOException e) {
            // exception statement
        }
    }

    // method for running the thread
    @Override
    public void run(){
        // checking for the receiving messages
        while(true){
            try {
                // receive and read the message from the personB using read() method
                String message = dataInputStream.readUTF();
                // display the received text in the textArea
                textArea.append("PersonB: " + message+"\n");

            } catch (IOException e) {
                // exception statement
            }
        }
    }

    public static void main(String[] args){
        new PersonA();
    }
}