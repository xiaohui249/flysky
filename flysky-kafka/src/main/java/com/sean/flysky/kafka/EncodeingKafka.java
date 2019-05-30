package com.sean.flysky.kafka;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

public class EncodeingKafka implements Serializer<Object>{

	@Override
	public void close() {}

	@Override
	public void configure(Map<String, ?> arg0, boolean arg1) {}

	@Override
	public byte[] serialize(String arg0, Object arg1) {
		return BeanUtils.bean2Byte(arg1);
	}

}
