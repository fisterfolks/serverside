package serverchat;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.*;
public class ClientThread extends Thread {
    private Socket socket;
    private Message message;
    private String login;
    private final String serverName = "Server";
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    public ClientThread(Socket socket) {
        this.socket = socket;
        this.start();
    }
    public void run() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            //Отправить сообщение, о входе
            message = (Message) inputStream.readObject();
            switch (message.getMessage()) {
                case "registration":
                    break;
            }
            while (true) {
                message = (Message) inputStream.readObject();
                System.out.println("[" + login + "]: " + message.getMessage());
                message.setOnlineUsers(ServerChat.getUserList().getUsers());
                if (message.getMessage().indexOf("@senduser") == -1)
                    broadcast(ServerChat.getUserList().getClientsList(), message);
                else
                    sendUser(message);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendUser(Message message) {
        String text = message.getMessage();
        String nameUser = text.split(" ")[1];

        System.out.print(nameUser + "*");
        try {
            ServerChat.getUserList().getClient(nameUser).getThisObjectOutputStream().writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void broadcast(ArrayList<Client> clientsArrayList, Message message) {
        try {
            for (Client client : clientsArrayList)
                client.getThisObjectOutputStream().writeObject(message);
        } catch (SocketException e) {
            System.out.println("in broadcast: " + login + " disconnected!");
            ServerChat.getUserList().deleteUser(login);
            broadcast(ServerChat.getUserList().getClientsList(), new Message("Server", "The user " + login + " has been disconnected", ServerChat.getUserList().getUsers()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}