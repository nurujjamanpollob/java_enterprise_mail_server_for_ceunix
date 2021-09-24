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

package org.apache.james.mime4j.codec;

import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class QuotedPrintableOutputStreamTest {

    @Test
    public void testEncode() throws IOException {
        ByteArrayOutputStream bos;
        QuotedPrintableOutputStream encoder;

        /*
         * Simple initial test.
         */
        bos = new ByteArrayOutputStream();
        encoder = new QuotedPrintableOutputStream(bos, false);
        encoder.write(fromString("This is the plain text message containing a few euros: 100 \u20ac!"));
        encoder.close();
        Assert.assertEquals("This is the plain text message containing a few euros: 100 =E2=82=AC!",
                toString(bos.toByteArray()));
    }

    @Test
    public void testEncodeUnderlyingStreamStaysOpen() throws IOException {
        ByteArrayOutputStream bos;
        QuotedPrintableOutputStream encoder;

        bos = new ByteArrayOutputStream();
        encoder = new QuotedPrintableOutputStream(bos, false);
        encoder.write(fromString("This is the plain text message containing a few euros: 100 \u20ac!"));
        encoder.close();

        try {
            encoder.write('b');
            Assert.fail();
        } catch (IOException expected) {
        }

        bos.write('y');
        bos.write('a');
        bos.write('d');
        bos.write('a');
        Assert.assertEquals("This is the plain text message containing a few euros: 100 =E2=82=AC!yada",
                toString(bos.toByteArray()));
    }

    @Test
    public void testEncodeSpecials() throws IOException {
        ByteArrayOutputStream bos;
        QuotedPrintableOutputStream encoder;

        bos = new ByteArrayOutputStream();
        encoder = new QuotedPrintableOutputStream(bos, false);
        encoder.write(fromString("Testing \u20ac special . chars = also at the end ="));
        encoder.close();
        Assert.assertEquals("Testing =E2=82=AC special =2E chars =3D also at the end =3D",
                toString(bos.toByteArray()));
    }

    @Test
    public void testEncodeWrapping() throws IOException {
        ByteArrayOutputStream bos;
        QuotedPrintableOutputStream encoder;

        bos = new ByteArrayOutputStream();
        encoder = new QuotedPrintableOutputStream(bos, false);
        encoder.write(fromString("This is a very very very very very very very very very very very very very very very long line"));
        encoder.close();
        Assert.assertEquals("This is a very very very very very very very very very very very very very =\r\nvery very long line",
                toString(bos.toByteArray()));
    }

    private byte[] fromString(String s) {
        try {
            return s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
            return null;
        }
    }

    private String toString(byte[] b) {
        try {
            return new String(b, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            fail(e.getMessage());
            return null;
        }
    }
}
