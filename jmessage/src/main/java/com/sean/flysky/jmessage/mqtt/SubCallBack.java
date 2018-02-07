package com.sean.flysky.jmessage.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class SubCallBack implements MqttCallback {
	
	private String intanceName = "";
	
	public SubCallBack(String instanceName) {this.intanceName = instanceName;}
	
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost : " + cause.getMessage() + "|" + ((MqttException)cause).getReasonCode() + "|" + cause.getCause());
		cause.printStackTrace();
	}

	@Override
	public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

	}

	public void messageArrived(MqttTopic topic, MqttMessage message)
			throws Exception {
		try {    
            System.out.println("Message arrived : " + message.toString() + " on Topic : " + topic.getName() + " for instance : " + this.intanceName);   
        } catch (Exception e) {    
            e.printStackTrace();    
        } 
		
	}

	public void deliveryComplete(MqttDeliveryToken token) {
		System.out.println("Delivery token " + token.hashCode() + " has been received : " + token.isComplete());
	}
	
}
