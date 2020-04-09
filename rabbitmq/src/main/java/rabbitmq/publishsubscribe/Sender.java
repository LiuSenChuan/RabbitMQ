package rabbitmq.publishsubscribe;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Sender {

    //fanout交换机	
	public static void main(String[] args) throws Exception {
		//建立连接
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		//创建链接
		Connection con = f.newConnection();
		//建立通道
		Channel c = con.createChannel();
		
		//定义fanout类型的交换机,注意现在的生产者不定义队列
		//rabbitmq服务器中,如果交换机不存在则新建   反之,空操作
		c.exchangeDeclare("logs", "fanout");
		
		//向交换机发送数据
		while (true) {
			System.out.println("输入:");
			String msg = new Scanner(System.in).nextLine();
			/**
			 * 参数:1.交换机   2.指定队列,指定任何对列名无效
			 *     3.其他参数    4.消息
			 */
			c.basicPublish("logs", "", null, msg.getBytes());
		}
	}
}
