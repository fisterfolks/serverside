package serverchat;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class ServerChat {
    private static UsersList list = new UsersList();
    private static final int IP_PORT = 4987;
   // private static DatabaseOfUsers db = new FileDatabase("base.txt");
    private static LocalDateTime date = LocalDateTime.now();
    public static void main(String[] args) throws IOException{
        FileWriter fw = new FileWriter("log.txt");
        fw.write("/////////////////////////////////////////////" + "\n");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd | HH:mm");
        try {
            fw.write("Server Started " + date.format(formatter));
            ServerSocket socketListener = new ServerSocket(IP_PORT);
            while (true) {
                Socket client = null;
                while (client == null)
                    client = socketListener.accept();
                new ClientThread(client); //Создаем новый поток, которому передаем сокет
            }
        } catch (SocketException e) {
            fw.write("Socket exception " + date.format(formatter));
            e.printStackTrace();
        }
        fw.close();
    }
    public synchronized static UsersList getUserList() {
        return list;
    }
   // public synchronized static DatabaseOfUsers getDatabase() {
     //   return db;
    //}
}