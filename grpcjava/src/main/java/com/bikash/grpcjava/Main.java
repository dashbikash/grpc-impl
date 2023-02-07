/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.bikash.grpcjava;
import com.bikash.grpcdemo.service.GreetingServiceGrpc;
import com.bikash.grpcdemo.service.HelloRequest;
import com.bikash.grpcdemo.service.HelloResponse;
import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author bikash
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        System.out.println("Hello GRPC Service");
        grpc_client();
        grpc_server();
    }
    private static void grpc_client() throws Exception{
        ManagedChannel channel=ManagedChannelBuilder.forAddress("localhost",8090).usePlaintext().build();
        GreetingServiceGrpc.GreetingServiceFutureStub stub=GreetingServiceGrpc.newFutureStub(channel);
        HelloRequest req=HelloRequest.newBuilder().setName("Bikash").build();
        HelloResponse resp =stub.sayHello(req).get();
        log.info(resp.getGreeting());
    }
    private static void grpc_server() throws Exception{
    Server server=ServerBuilder
                .forPort(8088)
                .addService(new GreetingServiceImpl())
                .build();
       server.start();
        log.info("Server started. Listening to "+server.getPort());
        server.awaitTermination();
    }
}
