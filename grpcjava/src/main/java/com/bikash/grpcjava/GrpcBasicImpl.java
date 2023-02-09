/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bikash.grpcjava;

import com.bikash.grpcdemo.service.FileRequest;
import com.bikash.grpcdemo.service.FileResponse;
import com.bikash.grpcdemo.service.GreetingServiceGrpc;
import com.bikash.grpcdemo.service.HelloRequest;
import com.bikash.grpcdemo.service.HelloResponse;
import com.google.common.io.Files;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.File;

/**
 *
 * @author bikash
 */
public class GrpcBasicImpl {

    public static void grpcServer() throws Exception {
        Server server = ServerBuilder
                .forPort(8088)
                .addService(new GreetingServiceImpl())
                .build();
        server.start();
        System.out.println("Server started. Listening to " + server.getPort());
        server.awaitTermination();
    }

    public static class ClientImpl {

        private static int port = 8088;

        public static void sayHello() throws Exception {
             
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
            GreetingServiceGrpc.GreetingServiceFutureStub stub = GreetingServiceGrpc.newFutureStub(channel);
            HelloRequest req = HelloRequest.newBuilder().setName("Bikash").build();
            HelloResponse resp = stub.sayHello(req).get();
            System.err.println(resp.getGreeting());
        }

        public static void fileUpload() throws Exception {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
            GreetingServiceGrpc.GreetingServiceFutureStub stub = GreetingServiceGrpc.newFutureStub(channel);
            byte[] upfile = Files.toByteArray(new File("/home/bikash/Documents/dev/grpc-impl/files/image.jpeg"));
            FileRequest req = FileRequest.newBuilder().setFile(ByteString.copyFrom(upfile)).build();
            FileResponse resp = stub.uploadFile(req).get();
            System.out.println(resp.getName() + " - " + resp.getUri());
        }
    }

}
