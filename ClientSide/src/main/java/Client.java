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
				
				System.out.println("Response from server: " + new String(response, ZMQ.CHARSET)); 
				
			}
		}
		else {
			System.out.println("Please specify URL");
		}

	}

}
