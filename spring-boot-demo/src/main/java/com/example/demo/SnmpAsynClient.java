package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;
 
public class SnmpAsynClient {
 
	public static final int DEFAULT_VERSION = SnmpConstants.version2c;
	public static final String DEFAULT_PROTOCOL = "udp";
	public static final int DEFAULT_PORT = 161;
	public static final long DEFAULT_TIMEOUT = 3 * 1000L;
	public static final int DEFAULT_RETRY = 3;
	private BlockingQueue<PDU> queue = new LinkedBlockingQueue<PDU>(50000);
	private Snmp snmp = null;
	public SnmpAsynClient() throws IOException {
		UdpAddress udpAddress = (UdpAddress)GenericAddress.parse("udp:0.0.0.0/10000");
		DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping(udpAddress);
		snmp = new Snmp(transport);
		snmp.listen();
		
		new Thread(new Receiver()).start();
	}
	
	class Receiver implements Runnable {

		@Override
		public void run() {
			while(true) {
				try {
					PDU pdu = queue.take();
					for (int i = 0; i < pdu.size(); i++) {
						VariableBinding vbResponse = pdu.get(i);
						System.out.println("vbResponse: " + vbResponse.getOid() + " = " + vbResponse.getVariable());
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
	
	public void snmpAsynSetList(String ip, String community, List<String> oidList, Callable<?> callable) {
		CommunityTarget target = createDefault(ip, community);
		try {
			
 
			PDU pdu = new PDU();
			for (String oid : oidList) {
				//TODO
				pdu.add(new VariableBinding(new OID(oid), new Integer32()));
			}
			
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
							System.out.println("request: " + vbRequest.getOid() + " = " + vbRequest.getVariable());
//							System.out.println("response: " + vbResponse.getOid() + " = " + vbResponse.getVariable());
						}
					}
//					callable.call();
				}
			};
 
			pdu.setType(PDU.GET);
			snmp.send(pdu, target, null, listener);
			
 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SNMP Asyn GetList Exception:" + e);
		}
 
	}
 
	/**
	 * 异步采集信息
	 *
	 * @param ip
	 * @param community
	 * @param oid
	 */
	public void snmpAsynGetList(String ip, String community, List<String> oidList) {
		CommunityTarget target = createDefault(ip, community);
		try {
			
 
			PDU pdu = new PDU();
			for (String oid : oidList) {
				pdu.add(new VariableBinding(new OID(oid)));
			}
			
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
							System.out.println("request: " + vbRequest.getOid() + " = " + vbRequest.getVariable());
//							System.out.println("response: " + vbResponse.getOid() + " = " + vbResponse.getVariable());
						}
					}
					
					try {
						queue.put(response);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
 
			pdu.setType(PDU.GET);
			snmp.send(pdu, target, null, listener);
			
 
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
		SnmpAsynClient client = new SnmpAsynClient();
		client.snmpAsynGetList(ip, community, oidList);
		
		Thread.currentThread().sleep(3000);
		client.close();
		
		
	
	}
}