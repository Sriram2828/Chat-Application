import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PersonB extends Frame implements Runnable, ActionListener{
    TextField textField;
    TextArea textArea;
    Button sendButton;

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    Thread chatThread;

    // Constructor
    PersonB(){
        textField = new TextField();
        textArea = new TextArea();
        sendButton = new Button("send");

        // actionlistener interface is used to create listener for the send button
        sendButton.addActionListener(this);

        try {
            // implementing the client sockets for communication
            socket = new Socket("localhost", 10000);

            // implementing the dataStreams for data transmission
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {

        }
        

        // add all components to the fram
        add(textField);
        add(textArea);
        add(sendButton);

        // created thread for the listener 
        chatThread = new Thread(this);
        chatThread.setDaemon(true);
        chatThread.start();

        // userinterface
        setSize(500,500);
        setTitle("PersonB");
        setLayout(new FlowLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        // generating the transmission data in the textfield
        String message = textField.getText();
        // display the generated text in the textArea
        textArea.append("PersonB: " + message+"\n");
        // empty the textfield after the message transmission occured
        textField.setText("");

        // send the genetated message
        try {
            // writeUTF() method is used to write the message into the outputStream
            dataOutputStream.writeUTF(message);
            // flush() method will send the message immediately, without storing it in the buffer
            dataOutputStream.flush();

        } catch (IOException e) {

        }
    }

    public static void main(String[] args){
        new PersonB();
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
                textArea.append("PersonA: " + message+"\n");

            } catch (IOException e) {

            }
        }
    }

}