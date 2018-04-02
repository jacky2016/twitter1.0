package com.xunku.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.transport.TransportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xunku.constant.PortalCST;
import com.xunku.utils.PropertiesUtils;

@SuppressWarnings("unchecked")
public class MQController {

	static MQController ctl;

	public synchronized static MQController getInstance() {
		if (ctl == null)
			ctl = new MQController();

		return ctl;
	}

	public class MQData {
		public String queueName;
		public String value;
		public String uid;

		public MQData(String _queueName, String _uid, String _value) {
			this.queueName = _queueName;
			this.uid = _uid;
			this.value = _value;
		}
	}

	private static final Logger LOG = LoggerFactory
			.getLogger(MQController.class);

	// 做一个缓存，存在问题是宕机缓存消失，看业务这样是否可以？否则还得实现持久化
	// 配合 ConcurrentHashMap 使用
	BlockingQueue<MQData> basket = new LinkedBlockingQueue<MQData>();

	ConcurrentHashMap threadMap = new ConcurrentHashMap();

	QueueFeeder feeder = new QueueFeeder();

	@SuppressWarnings("unchecked")
	private static ProducerTool _producerTool = null;
	private static JmsReceiver _JmsReceiver = null;
	private String _ServerAddress = "";
	private MQController() {
		//_producerTool = new ProducerTool();
		//feeder.start();
		try { 
			try {
				//"tcp://192.168.2.84:61616";
				this._ServerAddress = PropertiesUtils.getString("config",PortalCST.MQ_SERVER_ADDRESS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			_producerTool = new ProducerTool();
			
			feeder.start();
		} catch (Exception e) {
//			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}

	private MQController(boolean isSender, boolean isReceiver, String serverIp) {

		if (!Utility.isNullOrEmpty(serverIp)) {
			this._ServerAddress = serverIp;
		}
		else{
			try {
				//"tcp://192.168.2.84:61616";
				this._ServerAddress = PropertiesUtils.getString("config",PortalCST.MQ_SERVER_ADDRESS);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (isSender) {
			_producerTool = new ProducerTool();
			feeder.start();
		}
		if (isReceiver) {
			_JmsReceiver = new JmsReceiver();
		}
	}

	public static void main(String[] args) throws Exception {
		MQController mq = new MQController(true, false,
				"tcp://192.168.2.84:61616");
		System.out.println("1:" + new Date(System.currentTimeMillis()));
		for (int i = 0; i < 500; i++) {

			mq
					.sendMsg(
							"test1",
							String.valueOf(i),
							1 + ":apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog apple dog ");
		}
		System.out.println("2:" + new Date(System.currentTimeMillis()));

	}

	public void sendMsg(String queueName, String uid, String message)
			throws InterruptedException {
		// System.out.println("1w->"+new Date(System.currentTimeMillis()));
		if (!threadMap.containsKey(uid)) {
			MQData data = new MQData(queueName, uid, message);
			basket.put(data);
			threadMap.put(uid, "");
		}
	}

	public String getMsg(String queueName) {
		return _JmsReceiver.getMsg(queueName);
	}

	private class QueueFeeder extends Thread {

		@Override
		public void run() {
			int i = 0;
			while (true) {
				i++;

				try {
					List<MQData> lst = loadData();
					if (!lst.isEmpty()) {
						// 目前吴剑确认只有一个队列名，咱不按队列分了
						_producerTool.SendMsg(lst, lst.get(0).queueName);
						LOG
								.info("send turn:" + i + " dataCounts:"
										+ lst.size());
					} else {
						LOG
								.info("send turn:" + i + " dataCounts:"
										+ lst.size());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					LOG.error("QueueFeeder Thread 异常：" + ex.getMessage());
				}
				try {
					Thread.sleep(1000 * 10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private List<MQData> loadData() {
			List<MQData> lst = new ArrayList<MQData>();
			if (!threadMap.isEmpty()) {
				// 一次1000个
				for (int i = 0; i < 500; i++) {
					if (basket.isEmpty())
						break;
					try {
						MQData data = basket.take();
						lst.add(data);
						threadMap.remove(data.uid);
					} catch (InterruptedException e) {
						e.printStackTrace();
						LOG.error("BlockingQueue 出队列异常：" + e.getMessage());
					}
				}
			}
			return lst;
		}

		public void reportStatus() {
			// 状态汇报， 该记录些啥？？？
		}
	}

	private class ProducerTool {
		private Connection connection = null;
		private ActiveMQConnectionFactory connectionFactory = null;
		private boolean isConnected = false;

		public ProducerTool() {
			String mqAddress = _ServerAddress;// "192.168.2.158";//
			// _conf.get("mqservice.address.ip");
			// ConnectionFactory ：连接工厂，JMS 用它创建连接
			connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, mqAddress);
			// connectionFactory.setAlwaysSessionAsync(true);
			connectionFactory.setAlwaysSyncSend(true);
			// connectionFactory.setUseAsyncSend(true);
			connectionFactory.setUseCompression(true);

			// JMS 客户端到JMS Provider 的连接

			try {
				connection = connectionFactory.createConnection();
				connection.start();
				isConnected = true;
			} catch (JMSException e) {
				if (LOG.isInfoEnabled()) {
					LOG.error("ProducerTool MQ连接失败：" + e.getMessage());
				}
				isConnected = false;
			}
		}

		public Boolean getConnection() {
			// JMS 客户端到JMS Provider 的连接
			try {
				// if(connection!=null)
				connection = null;
				connection = connectionFactory.createConnection();
				connection.start();
				isConnected = true;
			} catch (JMSException e) {
				if (LOG.isInfoEnabled()) {
					LOG.info("JmsReceiver MQ连接失败：" + e.getMessage());
				}
				isConnected = false;
			}
			return isConnected;
		}

		@SuppressWarnings( { "unused" })
		private Session session = null;

		public void SendMsg(List<MQData> msg, String queueName) {
			try {
				if (!isConnected) {
					if (LOG.isInfoEnabled()) {
						LOG.error("ProducerTool MQ连接失败：尝试重连......");
					}
					this.getConnection();
					// session =
					// connection.createSession(Boolean.FALSE,Session.AUTO_ACKNOWLEDGE);
				}
				// if (session == null) {
				// session = connection.createSession(Boolean.FALSE,
				// Session.AUTO_ACKNOWLEDGE);
				// }
				// Session： 一个发送或接收消息的线程
				Session session = connection.createSession(Boolean.FALSE,
						Session.AUTO_ACKNOWLEDGE);
				// Destination ：消息的目的地;消息发送给谁.
				// 获取session注意参数值my-queue是Query的名字
				Destination destination = session.createQueue(queueName
						.toString());
				// MessageProducer：消息生产者
				MessageProducer producer = session.createProducer(destination);
				// 设置持久化
				producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
				// 发送一条消息
				int size = msg.size();
				for (int i = 0; i < size; i++) {
					sendMsg(session, producer, msg.get(i).value);
				}
				session.commit();
				session.close();
				// connection.close();
				return;
			} catch (JMSException e) {
				if (LOG.isInfoEnabled()) {
					LOG.error("通知MQ接受处理异常：" + e.getMessage());
				}
				try {
					connection = connectionFactory.createConnection();
					connection.start();
					isConnected = true;
					LOG.info("通知MQ接受处理异常，尝试重连成功....");
				} catch (JMSException ex) {
					if (LOG.isInfoEnabled()) {
						LOG.error("通知MQ接受处理异常，尝试重连失败....");
					}
					isConnected = false;
				}
			}
		}

		private void sendMsg(Session session, MessageProducer producer,
				String msg) throws JMSException {
			// 创建一条文本消息
			TextMessage message = session.createTextMessage(msg);
			// 通过消息生产者发出消息
			producer.send(message);
			if (LOG.isInfoEnabled()) {
				LOG.info("通知MQ接受处理：" + msg);
			}
		}
	}

	private class JmsReceiver {
		private Connection connection = null;
		private ConnectionFactory connectionFactory = null;
		private boolean isConnected = false;
		private String mqAddress;

		public JmsReceiver() {
			mqAddress = "192.168.2.158";// _conf.get("mqservice.address.ip");
			// ConnectionFactory ：连接工厂，JMS 用它创建连接
			connectionFactory = new ActiveMQConnectionFactory(
					ActiveMQConnection.DEFAULT_USER,
					ActiveMQConnection.DEFAULT_PASSWORD, mqAddress);
			try {
				connection = connectionFactory.createConnection();
				((ActiveMQConnection) connection)
						.addTransportListener(new TransportListener() {
							public void onCommand(Object cmd) {
								// LOG.info("MQ Command onCommand:" +
								// cmd.toString() );
							}

							public void onException(IOException exp) {
								LOG.error("MQ Listener Exception："
										+ exp.getMessage());
							}

							public void transportInterupted() {
								// The transport has suffered an interruption
								// from which it hopes to recover.
								LOG.info("MQ Listener transportInterupted...");
								isConnected = false;
							}

							public void transportResumed() {
								// The transport has resumed after an
								// interruption.
								LOG.info("MQ Listener transportResumed...");
								isConnected = true;
							}
						});

				connection.start();
				isConnected = true;
			} catch (JMSException e) {
				if (LOG.isInfoEnabled()) {
					LOG.info("JmsReceiver MQ连接失败>>>>>：" + e.getMessage());
				}
				isConnected = false;
			}
		}

		public Boolean getConnection() {
			// JMS 客户端到JMS Provider 的连接
			try {
				// if(connection!=null)
				connection = null;
				connection = connectionFactory.createConnection();
				connection.start();
				isConnected = true;
			} catch (JMSException e) {
				if (LOG.isInfoEnabled()) {
					LOG.info("JmsReceiver MQ连接失败：" + e.getMessage());
				}
				isConnected = false;
			}

			return isConnected;
		}

		private String getMsg(String queueName) {
			if (!isConnected) {
				if (LOG.isInfoEnabled()) {
					LOG.info("JmsReceiver MQ未连接，暂不能接收,请尽快排查......");
					this.getConnection();
				}
				return "";
			}

			if (LOG.isInfoEnabled()) {
				LOG.info("JmsReceiver 启动成功......");
			}

			try {
				Session session = connection.createSession(Boolean.TRUE,
						Session.SESSION_TRANSACTED);
				// Destination ：消息的目的地;消息发送给谁.
				// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
				Destination destination = session.createQueue(queueName);
				MessageConsumer consumer = session.createConsumer(destination);
				TextMessage message = (TextMessage) consumer.receive(1000);
				session.commit();
				consumer.close();
				session.close();
				if (null != message) {
					String mStr = message.getText();
					return mStr;
				}
				return "";
			} catch (JMSException e) {
				if (LOG.isInfoEnabled()) {
					LOG.error("JmsReceiver MQ连接失败了：" + e.getMessage());
				}
				isConnected = false;
				return "";
			}
		}

		@SuppressWarnings( { "unused", "deprecation" })
		private void ReciveAndDo(String queueName) {
			if (!isConnected) {
				if (LOG.isInfoEnabled()) {
					LOG.info("JmsReceiver MQ未连接，暂不能接收,请尽快排查......");
					this.getConnection();
				}
				return;
			}

			if (LOG.isInfoEnabled()) {
				LOG.info("JmsReceiver 启动成功......");
			}

			String begin = new Date().toLocaleString();
			while (true) {
				try {
					if (LOG.isInfoEnabled()) {
						LOG.info("JmsReceiver 接收中.....");
					}
					if (!isConnected) {
						LOG.info("JmsReceiver Connection连接失败  稍后接收..........");
						try {
							// this.getConnection();
							Thread.sleep(5000);
							LOG.info("JmsReceiver Connection尝试重连Begin.....");
							this.getConnection();
							LOG.info("JmsReceiver Connection尝试重连End.....");
						} catch (InterruptedException e) {
							LOG.error("JmsReceiver sleep 异常：" + e.getMessage());
						}
						continue;
					}
					Session session = connection.createSession(Boolean.TRUE,
							Session.SESSION_TRANSACTED);
					// Destination ：消息的目的地;消息发送给谁.
					// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
					Destination destination = session.createQueue(queueName);
					MessageConsumer consumer = session
							.createConsumer(destination);
					TextMessage message = (TextMessage) consumer.receive(1000);
					session.commit();
					consumer.close();
					session.close();
					if (null != message) {
						String mStr = message.getText();
						if (LOG.isInfoEnabled()) {
							LOG.info("JmsReceiver 接收到MQ消息，开始处理：" + mStr);
							LOG.info("JmsReceiver 上次接收时间：" + begin);
							begin = new Date().toLocaleString();
							LOG.info("JmsReceiver 本次接收时间：" + begin);
						}
						// 处理消息
						if (mStr.startsWith("index:")) {
							// do....;
						} else if (mStr.startsWith("merge:")) {
							// do....;
						}
					} else
						try {
							Thread.sleep(5 * 1000);// 5分钟轮一次
							System.gc();// 强制垃圾回收
						} catch (InterruptedException e) {
							if (LOG.isInfoEnabled()) {
								LOG.info("MQ线程异常：" + e.getMessage());
							}
						}
				} catch (JMSException e) {
					if (LOG.isInfoEnabled()) {
						LOG.error("JmsReceiver MQ连接失败了：" + e.getMessage());
					}
					isConnected = false;
				}
			}
		}
	}
}