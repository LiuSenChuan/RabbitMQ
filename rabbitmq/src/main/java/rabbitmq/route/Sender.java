package rabbitmq.route;

import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

	//·��ģʽ
	//ֱ��������
	public static void main(String[] args) throws Exception{
		//��������
				ConnectionFactory f = new ConnectionFactory();
				f.setHost("192.168.64.140");
				f.setPort(5672);
				f.setUsername("admin");
				f.setPassword("admin");
				
				//��������
				Connection con = f.newConnection();
				//����ͨ��
				Channel c = con.createChannel();
				
				//���彻����
				c.exchangeDeclare("direct-logs", BuiltinExchangeType.DIRECT);
				
				//������Ϣ
				while (true) {
				   System.out.println("������Ϣ:");	
				   String  msg = new Scanner(System.in).nextLine();
				   System.out.println("����key:");	
				   String  key = new Scanner(System.in).nextLine();
				   c.basicPublish("direct-logs", key, null, msg.getBytes());
				   
				}
	}
}
