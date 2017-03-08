import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by 80561 on 2016/12/20.
 */
public class User {

    private Socket socket;
    private String userName;

    public User(String userName, Socket socket){
        this.userName = userName;
        this.socket = socket;
    }

    public String getUserName() {
        return userName;
    }

    public void sendmsg(String whoSend, String content) {
   //     System.out.println(whoSend);
        PrintWriter pout = null;
        try {
                pout = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),true);
                pout.println(whoSend);
                pout.println(content);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
