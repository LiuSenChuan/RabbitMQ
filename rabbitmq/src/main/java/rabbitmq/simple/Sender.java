package rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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

		/*
		 * ��������,����rabbitmq�д���һ������
		 * ����Ѿ��������ö��У��Ͳ�����ʹ����������������
		 * 
		 * ��������:
		 *   -queue: ��������
		 *   -durable: ���г־û�,true��ʾRabbitMQ����������Դ���
		 *   -exclusive: ����,true��ʾ���ƽ���ǰ���ӿ���
		 *   -autoDelete: �����һ�������߶Ͽ���,�Ƿ�ɾ������
		 *   -arguments: ��������
		 */
		c.queueDeclare("hellworld", false, false, false, null);
		
		/*
		 * ������Ϣ
		 * �������Ϣ��Ĭ�Ͻ���������.
		 * Ĭ�Ͻ��������������ж��а�,routing key��Ϊ��������
		 * 
		 * ��������:
		 * 	-exchange: ����������,�մ���ʾĬ�Ͻ�����"(AMQP default)",������ null 
		 * 	-routingKey: ����Ĭ�Ͻ�����,·�ɼ�����Ŀ���������
		 * 	-props: ��������,����ͷ��Ϣ
		 * 	-body: ��Ϣ����byte[]����
		 */
		c.basicPublish("", "hellworld", null,
				   ("hellworld"+System.currentTimeMillis()).getBytes());
		System.out.println("��Ϣ�ѷ���");
		
		//�Ͽ�����
		c.close();
		con.close();
		
	}
}
