import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 80561 on 2016/12/20.
 */
public class MySocketServer {
        private static final int PORT = 22236;
        private List<Socket> mList = new ArrayList<Socket>();
        public List<User> users = new ArrayList<>();
        private ServerSocket server = null;
        private ExecutorService mExecutorService = null; //thread pool

        public static void main(String[] args) {
            new MySocketServer();
        }
        public MySocketServer() {
            try {
                server = new ServerSocket(PORT);
                mExecutorService = Executors.newCachedThreadPool();  //create a thread pool
                System.out.print("server start ...");
                Socket client = null;
                while(true) {
                    client = server.accept();
                    mList.add(client);
                    mExecutorService.execute(new Service(client)); //start a new thread to handle the connection
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        class Service implements Runnable {
            private Socket socket;
            private BufferedReader in = null;
            private String msg = "";
            private int count = 0;
            private String whoSend;
            private String content;
            private String sendTo;

            public Service(Socket socket) {
                this.socket = socket;
                try {
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if ((msg = in.readLine())!= null){
                        User user = new User(msg, socket);
                        users.add(user);
                        System.out.println("\nconnect" + msg);
                    }
             /*       msg = "user" +this.socket.getInetAddress() + "come toal:"
                            +mList.size();*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    while(true) {
                        if((msg = in.readLine())!= null) {
                            //接受消息
                            if (count == 0){
                                whoSend = msg;
                                System.out.println("whoSend" + whoSend);
                                count++;
                            }else if (count == 1){
                                content = msg;
                                System.out.println("content" + content);
                                count++;
                            }else if (count == 2){
                                sendTo = msg;
                                System.out.println("sendTo" + sendTo);
                                count++;
                            }
                            if (count == 3){
                                for (int index = 0; index < users.size(); index ++) {
                                    User user = users.get(index);
                                    if (sendTo.equals(user.getUserName())){
                                        user.sendmsg(whoSend, content);
                                    }
                                }
                                count = 0;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
}
