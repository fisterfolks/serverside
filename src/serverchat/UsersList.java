package serverchat;
import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UsersList {
    private Map<String, Client> onlineUsers = new ConcurrentHashMap<>();
    public void addUser(String login, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        System.out.println( login + " connected" );
        if (!this.onlineUsers.containsKey(login))
            this.onlineUsers.put(login , new Client(socket, oos, ois));
        else {
            int i = 1;
            while(this.onlineUsers.containsKey(login)) {
                login = login + i;
                i++;
            }
            this.onlineUsers.put(login , new Client(socket, oos, ois));
        }
    }
    public void deleteUser(String login) {
        this.onlineUsers.remove(login);
    }
    public String[] getUsers() {
        return this.onlineUsers.keySet().toArray(new String[0]);
    }

    public Client getClient(String login) {
        return onlineUsers.get(login);
    }
    public ArrayList<Client> getClientsList() {
        ArrayList<Client> clientsList = new ArrayList<Client>(this.onlineUsers.entrySet().size());

        String s = "";
        for(Map.Entry<String, Client> m : this.onlineUsers.entrySet()){
            clientsList.add(m.getValue());
            s = s + m.getKey();
        }

        return clientsList;
    }

}
