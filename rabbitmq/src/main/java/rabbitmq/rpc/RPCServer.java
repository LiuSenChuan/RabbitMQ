package rabbitmq.rpc;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RPCServer {

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
		//1.���յ�����Ϣ
		//2.ִ��ҵ������
		//3.�ɽ������ȥ
		//�������
		c.queueDeclare("rpc_queue", false, false, false, null);
		
		DeliverCallback deliverCallback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
               String s = new String(message.getBody());
               int n = Integer.parseInt(s);
               //���N��쳲�������
               long r =fbnq(n);
               
               //ȥ�����ض������͹���id
               //replyTo���ض�����
               String replyTo = message.getProperties().getReplyTo();
               String correlationId  = message.getProperties().getCorrelationId();
               //��correlationId����BasicProperties����
               BasicProperties props = new BasicProperties().builder().correlationId(correlationId).build();
               c.basicPublish("", replyTo, props, (""+r).getBytes());
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		
		c.basicConsume("rpc_queue", true, deliverCallback, cancelCallback);
		
		
	}
	//���n��쳲�������
	//1,1,2,3,5,8.....
	static long fbnq(int n) {
		if (n == 1 || n == 2) {
			return 1;
		}
		return fbnq(n-1)+fbnq(n-2);
	}
	
}
