package rabbitmq.publishsubscribe;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

    //fanout������	
	public static void main(String[] args) throws Exception {
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
		
		//����fanout���͵Ľ�����,ע�����ڵ������߲��������
		//rabbitmq��������,������������������½�   ��֮,�ղ���
		c.exchangeDeclare("logs", "fanout");
		
		//�򽻻�����������
		while (true) {
			System.out.println("����:");
			String msg = new Scanner(System.in).nextLine();
			/**
			 * ����:1.������   2.ָ������,ָ���κζ�������Ч
			 *     3.��������    4.��Ϣ
			 */
			c.basicPublish("logs", "", null, msg.getBytes());
		}
	}
}
