import java.util.ArrayList;

import org.zeromq.*;
import org.zeromq.ZMQ.Socket;

public class Client {

	public static void main(String[] args) {
		if(args.length > 0) {
			try(ZContext context = new ZContext()){
				Socket socket = context.createSocket(SocketType.DEALER);
				socket.connect(args[0]);
				String msg = "{\"subscribe\":true}";
				socket.send(msg);
				
				byte[] response = socket.recv();
				
				String returnedHash = new String(response, ZMQ.CHARSET);
				ArrayList<String> students = new ArrayList<String>();
				//students.add(returnedHash.endsWith());
				System.out.println("Queue\n" + returnedHash);
			}
		}
		else {
			System.out.println("Please specify URL");
		}

	}

}
