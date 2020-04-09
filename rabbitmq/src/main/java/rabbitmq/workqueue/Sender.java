package rabbitmq.workqueue;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Sender {

	//������
	public static void main(String[] args) throws IOException, TimeoutException {
		//����rabbitmq������
		ConnectionFactory f =new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		//��������
		Connection con = f.newConnection();
		//����ͨ��
		Channel c= con.createChannel();

	    //��������
		c.queueDeclare("hellworld", false, false, false, null);
		while (true) {
			System.out.println("����:");
			String s = new Scanner(System.in).nextLine();
			//������Ϣ
			c.basicPublish("", "hellworld", MessageProperties.PERSISTENT_TEXT_PLAIN,s.getBytes());
		}
		
		
	}
}
