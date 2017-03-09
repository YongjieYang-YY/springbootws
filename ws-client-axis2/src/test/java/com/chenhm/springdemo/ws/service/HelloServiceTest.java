package com.chenhm.springdemo.ws.service;

import java.io.IOException;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.chenhm.springdemo.ws.HelloServiceStub;

import sample.ws.service.SayHello;
import sample.ws.service.SayHelloDocument;
import sample.ws.service.SayHelloResponseDocument;


public class HelloServiceTest {
    public static SayHelloDocument dummyReq() {
        SayHelloDocument doc = SayHelloDocument.Factory.newInstance();
        SayHello hello = SayHello.Factory.newInstance();
        hello.setMyname("Frank");
        doc.setSayHello(hello);
        return doc;
    }

    @Before
    public void setup() {
        System.setProperty("javax.net.ssl.trustStorePassword", "123");
        System.setProperty("javax.net.ssl.trustStore", "src/main/resources/localhost.jks");
    }

    @Test
    public void testSayHello() throws IOException {

        HelloServiceStub stub = null;
        try {
            stub = new HelloServiceStub();
        } catch (AxisFault e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertNotNull(stub);

        SayHelloResponseDocument resp = null;
        try {
            resp = stub.sayHello(dummyReq());
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail();
        }
        Assert.assertNotNull(resp);
        Assert.assertEquals("Hello, Welcome to CXF Spring boot Frank!!!", resp.getSayHelloResponse().getReturn());
    }


}
