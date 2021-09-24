/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class LimitedInputStreamTest {

    @Test
    public void testUpToLimitRead() throws IOException {
        byte[] data = new byte[]{'0', '1', '2', '3', '4', '5', '6'};
        LimitedInputStream limitedStream = new LimitedInputStream(InputStreams.create(data), 3);
        Assert.assertEquals(0, limitedStream.getPosition());
        Assert.assertTrue(limitedStream.read() != -1);
        Assert.assertEquals(1, limitedStream.getPosition());
        byte[] tmp = new byte[3];
        Assert.assertEquals(2, limitedStream.read(tmp));
        Assert.assertEquals(3, limitedStream.getPosition());
        try {
            limitedStream.read();
            Assert.fail("IOException should have been thrown");
        } catch (IOException ex) {
        }
        try {
            limitedStream.read(tmp);
            Assert.fail("IOException should have been thrown");
        } catch (IOException ex) {
        }
        try {
            limitedStream.skip(2);
            Assert.fail("IOException should have been thrown");
        } catch (IOException ex) {
        }

        limitedStream.close();
    }

}
