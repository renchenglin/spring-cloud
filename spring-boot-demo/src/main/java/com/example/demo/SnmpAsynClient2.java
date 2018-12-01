package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
 
public class SnmpAsynClient2 {
 
	public static final int DEFAULT_VERSION = SnmpConstants.version2c;
	public static final String DEFAULT_PROTOCOL = "udp";
	public static final int DEFAULT_PORT = 161;
	public static final long DEFAULT_TIMEOUT = 10 * 1000L;
	public static final int DEFAULT_RETRY = 3;
	private BlockingQueue<Task> queue = new LinkedBlockingQueue<Task>(50000);
	private Snmp snmp = null;
	public SnmpAsynClient2() throws IOException {
		UdpAddress udpAddress = (UdpAddress)GenericAddress.parse("udp:0.0.0.0/10000");
		DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping(udpAddress);
		snmp = new Snmp(transport);
		snmp.listen();
		
		new Thread(new Sender()).start();
	}
	
	class Task {
		private final PDU request;
		private final CommunityTarget target;
		private final ResponseListener listener;
		private long taskId;
		
		public Task(final PDU request, final CommunityTarget target, final ResponseListener listener, long taskId) {
			this.request = request;
			this.target = target;
			this.listener = listener;
		}
	}
	class Sender implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					Task task = queue.take();
					
					try {
						snmp.send(task.request, task.target, null, task.listener);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

		
	}
	
	
	
	public void close() throws IOException {
		snmp.close();
	}
 
	/**
	 * 创建对象communityTarget
	 *
	 * @param targetAddress
	 * @param community
	 * @param version
	 * @param timeOut
	 * @param retry
	 * @return CommunityTarget
	 */
	public static CommunityTarget createDefault(String ip, String community) {
		Address address = GenericAddress.parse(DEFAULT_PROTOCOL + ":" + ip + "/" + DEFAULT_PORT);
		CommunityTarget target = new CommunityTarget();
		target.setCommunity(new OctetString(community));
		target.setAddress(address);
		target.setVersion(DEFAULT_VERSION);
		target.setTimeout(DEFAULT_TIMEOUT); // milliseconds
		target.setRetries(DEFAULT_RETRY);
		return target;
	}
	
	/**
	 * 异步采集信息
	 *
	 * @param ip
	 * @param community
	 * @param oid
	 */
	public void snmpAsynGetList(String ip, String community, List<String> oidList, long taskId) {
		CommunityTarget target = createDefault(ip, community);
		try {
			
 
			PDU pdu = new PDU();
			for (String oid : oidList) {
				pdu.add(new VariableBinding(new OID(oid)));
			}
			pdu.setType(PDU.GET);
			ResponseListener listener = new ResponseListener() {
				public void onResponse(ResponseEvent event) {
					((Snmp) event.getSource()).cancel(event.getRequest(), this);
					PDU response = event.getResponse();
					PDU request = event.getRequest();
					if (response == null) {
						System.out.println("[ERROR]: response is null");
					} else if (response.getErrorStatus() != 0) {
						System.out.println("[ERROR]: response status"
								+ response.getErrorStatus() + " Text:"
								+ response.getErrorStatusText());
					} else {
						for (int i = 0; i < response.size(); i++) {
							VariableBinding vbResponse = response.get(i);
							VariableBinding vbRequest = request.get(i);
//							System.out.println("request: " + vbRequest.getOid() + " = " + vbRequest.getVariable());
//							System.out.println("response: " + vbResponse.getOid() + " = " + vbResponse.getVariable());
						}
						System.out.println("taskId: " + taskId);
						if(taskId == 1000) {
							System.out.println("end: " + System.currentTimeMillis());
						}
					}
					
				}
			};
 
			queue.put(new Task(pdu, target, listener, taskId));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SNMP Asyn GetList Exception:" + e);
		}
 
	}
 
	/**
	 *
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
 
		String ip = "192.168.31.108";
		String community = "public";
 
		List<String> oidList = new ArrayList<String>();
		oidList.add(".1.3.6.1.2.1.1.1.0");
		oidList.add(".1.3.6.1.2.1.1.3.0");
		oidList.add(".1.3.6.1.2.1.1.5.0");
		// 异步采集数据
		SnmpAsynClient2 client = new SnmpAsynClient2();
		System.out.println("start: " + System.currentTimeMillis());
		for(int i=0; i<=5000;i++) {
			if(i%2==0) {
				client.snmpAsynGetList("192.168.31.107", community, oidList, i);
			} else {
				client.snmpAsynGetList("192.168.31.108", community, oidList, i);
			}
		}
	
	}
}