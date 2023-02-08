package com.bikash.grpcjava;

import com.bikash.grpcdemo.service.FileRequest;
import com.bikash.grpcdemo.service.FileResponse;
import com.bikash.grpcdemo.service.GreetingServiceGrpc;
import com.bikash.grpcdemo.service.HelloRequest;
import com.bikash.grpcdemo.service.HelloResponse;
import com.google.common.io.Files;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bikash
 */
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void uploadFile(FileRequest request, StreamObserver<FileResponse> responseObserver) {
        byte[] data = request.getFile().toByteArray();
        try {
            Files.write(data, new File("/tmp/java_grpc_image.jpeg"));
        } catch (IOException ex) {
            Logger.getLogger(GreetingServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileResponse resp=FileResponse.newBuilder().setName("java_grpc_image.jpeg").setUri("/tmp/java_grpc_image.jpeg").build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse resp = HelloResponse.newBuilder()
                .setGreeting("Hello from Java GRPC , " + request.getName())
                .build();
        responseObserver.onNext(resp);
        responseObserver.onCompleted();
    }

}
