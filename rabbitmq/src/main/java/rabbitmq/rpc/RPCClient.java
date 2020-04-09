package rabbitmq.rpc;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.CancelCallback;

public class RPCClient {
	public static BlockingQueue<Long> q =new ArrayBlockingQueue<>(10);

	public static void main(String[] args) throws Exception{
		System.out.println("��ڼ���쳲�������:");
		int  n = new Scanner(System.in).nextInt();
		long  r = fbnq(n);
		System.out.println(r);
	}
	
	public static long fbnq(int n) throws Exception {
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
				
				//1.���͵�����Ϣ
				c.queueDeclare("rpc_queue", false, false, false, null);
				//���ض������͹���id
				String replyTo = c.queueDeclare().getQueue();
				String correlationId = UUID.randomUUID().toString();
				//�����ض������͹���id������������
				BasicProperties props = 
						new BasicProperties()
						.builder()
						.replyTo(replyTo)
						.correlationId(correlationId)
						.build();
				c.basicPublish("", "rpc_queue", props, (""+n).getBytes());
				
				//2.ģ�����ִ�����߳���������
				System.out.println("��������.........");
				//3.ֱ��ȡ�����ʱ,���ؽ��
				DeliverCallback deliverCallback = new DeliverCallback() {
					@Override
					public void handle(String consumerTag, Delivery message) throws IOException {
					//���ص�쳲���������������id
						String msg = new String(message.getBody());
						String cid = message.getProperties().getCorrelationId();
						if (cid.equals(correlationId)) {
							//��Ϣ�����̷߳�����
							q.offer(Long.parseLong(msg));
						}
						try {
							c.close();
						} catch (Exception e) {
							e.printStackTrace();
						} 
						con.close();
					}
				};
				CancelCallback cancelCallback = new CancelCallback() {
					@Override
					public void handle(String consumerTag) throws IOException {
					}
				};
				
				c.basicConsume(replyTo,true, deliverCallback, cancelCallback);
				
				//���߳�������
				return q.take();
	}
}
