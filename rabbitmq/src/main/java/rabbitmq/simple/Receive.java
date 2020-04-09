package rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receive {

	//������
	public static void main(String[] args) throws IOException, TimeoutException {
		//����rabbitmq������
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		//��������
		Connection con = f.newConnection();
		//��������ͨ��
		Channel c = con.createChannel();
		 
	   //�������ж������
		c.queueDeclare("hellworld", false, false, false, null);
		
		//�յ���Ϣ������������Ϣ�Ļص�
		DeliverCallback deliverCallback =new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				byte[] a = message.getBody();
				String s = new String(a);
				System.out.println("��Ϣ���յ�:"+ s);
			}
		};
		//������ȡ��ʱ�Ļص�����
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
				
			}
		};
		//��ʼ������Ϣ
		c.basicConsume("hellworld",true , deliverCallback,cancelCallback);
	}
}
