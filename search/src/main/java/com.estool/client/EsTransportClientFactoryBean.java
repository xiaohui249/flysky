//package com.estool.client;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.ImmutableSettings;
//import org.elasticsearch.common.settings.ImmutableSettings.Builder;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.core.io.Resource;
//
//public class EsTransportClientFactoryBean implements FactoryBean, InitializingBean, DisposableBean {
//
//	private static final Logger logger = LoggerFactory.getLogger(EsTransportClientFactoryBean.class);
//
//	private TransportClient transportClient;
//
//	private Resource settings;
//
//	private Map<String, Integer> transportAddresses;
//
//	public void setTransportAddresses(final Map<String, Integer> transportAddresses) {
//		this.transportAddresses = transportAddresses;
//	}
//
//	private void internalCreateTransportClient() {
//		Settings settings = null;
//		Builder builder = ImmutableSettings.settingsBuilder();
//		try {
//			InputStream is = this.settings.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//			String line = null;
//			try {
//				while ((line = reader.readLine()) != null) {
//					if (line.startsWith("#")) {
//						continue;
//					}
//					String[] keyValue = line.split("=");
//					if (keyValue.length == 2) {
//						String key = keyValue[0];
//						String value = keyValue[1];
//						builder.put(key, value);
//					} else {
//						logger.error("read settings error,not a keyvalue pair");
//					}
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					is.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("read settings error.");
//		}
//
//		final TransportClient client;
//		settings = builder.build();
//		if (null != settings) {
//			client = new TransportClient(settings);
//		} else {
//			client = new TransportClient();
//		}
//
//		if (null != transportAddresses) {
//			for (final Entry<String, Integer> address : transportAddresses.entrySet()) {
//				if (logger.isInfoEnabled()) {
//					logger.info("Adding transport address: " + address.getKey() + " port: " + address.getValue());
//				}
//				client.addTransportAddress(new InetSocketTransportAddress(address.getKey(), address.getValue()));
//			}
//
//		}
//		this.transportClient = client;
//	}
//
//	public void destroy() throws Exception {
//		transportClient.close();
//	}
//
//	public TransportClient getObject() throws Exception {
//		return transportClient;
//	}
//
//	public Class<TransportClient> getObjectType() {
//		return TransportClient.class;
//	}
//
//	public boolean isSingleton() {
//		return true;
//	}
//
//	public Resource getSettings() {
//		return settings;
//	}
//
//	public void setSettings(final Resource settings) {
//		this.settings = settings;
//	}
//
//	public void afterPropertiesSet() throws Exception {
//		internalCreateTransportClient();
//	}
//
//}
