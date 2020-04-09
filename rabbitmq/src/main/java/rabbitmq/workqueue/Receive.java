package rabbitmq.workqueue;

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
		c.queueDeclare("hellworld", true, false, false, null);
		
		//�յ���Ϣ������������Ϣ�Ļص�
		DeliverCallback deliverCallback =new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String s = new String(message.getBody());
				System.out.println("��Ϣ���յ�:"+ s);
				for (int i = 0; i < s.length(); i++) {
					//ÿ����һ��.�ַ���ͣһ��
					if (s.charAt(i)=='.') {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				//�ֶ�ack����,�ֶ�����������ͻ�ִ   false��ʾ�Ƿ�ȷ�϶�����Ϣ(��)
				c.basicAck(message.getEnvelope().getDeliveryTag(), false);
				System.out.println("��Ϣ�������---------------------\n");
			}
		};
		//������ȡ��ʱ�Ļص�����
		CancelCallback cancelCallback = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
				
			}
		};
		//����ÿ��ֻ����һ������
		c.basicQos(1);
		//��ʼ������Ϣ
		//�ڶ�������true  �Զ�ack  false  �ֶ�ack
		c.basicConsume("hellworld",false , deliverCallback,cancelCallback);
	}
}
