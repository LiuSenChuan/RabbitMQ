package rabbitmq.publishsubscribe;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Receive {

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
		
		
		//1.�������   fanout���͵Ķ���   �������,�ǳ־�,��ռ,�Զ�ɾ��
		String queue = c.queueDeclare().getQueue();
		//2.���彻����
		c.exchangeDeclare("logs", "fanout");
		//3.��
		c.queueBind(queue, "logs", "");//��������ģʽ�е�����������Ч
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
                String msg = new String(message.getBody()); 
                System.out.println("�յ�:"+msg);
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		//��������
		c.basicConsume(queue, true, deliverCallback, cancelCallback);
	}
}
