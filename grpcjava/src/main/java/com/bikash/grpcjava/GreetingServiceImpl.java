package com.bikash.grpcjava;

import com.bikash.grpcdemo.service.FileRequest;
import com.bikash.grpcdemo.service.FileResponse;
import com.bikash.grpcdemo.service.GreetingServiceGrpc;
import com.bikash.grpcdemo.service.HelloRequest;
import com.bikash.grpcdemo.service.HelloResponse;
import io.grpc.stub.StreamObserver;

/**
 *
 * @author bikash
 */
public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {

    @Override
    public void uploadFile(FileRequest request, StreamObserver<FileResponse> responseObserver) {
        FileResponse resp=FileResponse.newBuilder().setName("myfile"+request.getExt()).build();
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
