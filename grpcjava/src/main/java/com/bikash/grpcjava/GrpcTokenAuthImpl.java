package com.bikash.grpcjava;

import com.bikash.grpcdemo.service.GreetingServiceGrpc;
import com.bikash.grpcdemo.service.HelloRequest;
import com.bikash.grpcdemo.service.HelloResponse;
import io.grpc.CallCredentials;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bikash
 */
public class GrpcTokenAuthImpl {

    public static void grpcServer() {

        try {
            Server server = ServerBuilder
                    .forPort(8088)
                    .addService(new GreetingServiceImpl())
                    .intercept(new BasicTokenAuthServerInterceptor())
                    .build();
            server.start();
            System.out.println("Server started. Listening to " + server.getPort());
            server.awaitTermination();
        } catch (Exception ex) {
            Logger.getLogger(GrpcTokenAuthImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final class Constant {

        static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
        static final Context.Key<String> USER_ID_CONTEXT_KEY = Context.key("userid");

        private Constant() {
            throw new AssertionError();
        }
    }

    public static class BasicTokenAuthServerInterceptor implements ServerInterceptor {

        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> sc, Metadata mtdt, ServerCallHandler<ReqT, RespT> sch) {
            String token = mtdt.get(Constant.AUTHORIZATION_METADATA_KEY);
            System.out.println("Client Pwd: " + token);
            Status status = Status.PERMISSION_DENIED;

            if (token == null) {
                status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
            } else {
                if (token.equals("mypassword")) {
                    Context ctx = Context.current()
                            .withValue(Constant.USER_ID_CONTEXT_KEY, "system");
                    return Contexts.interceptCall(ctx, sc, mtdt, sch);
                } else {
                    status = Status.UNAUTHENTICATED.withDescription("Token invalid");
                }
            }

            sc.close(status, mtdt);
            return new ServerCall.Listener<ReqT>() {
            };
        }
    }

    public static class ClientTokenCredentials extends CallCredentials {
        private String token;

        public ClientTokenCredentials(String token) {
            this.token = token;
        }
        
                       
        @Override
        public void applyRequestMetadata(RequestInfo ri, Executor exctr, MetadataApplier ma) {

            exctr.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Metadata headers = new Metadata();
                        headers.put(Constant.AUTHORIZATION_METADATA_KEY,token);
                        ma.apply(headers);
                    } catch (Throwable e) {
                        ma.fail(Status.UNAUTHENTICATED.withCause(e));
                    }
                }
            });
        }

        @Override
        public void thisUsesUnstableApi() {

        }

    }
    
    public static class ClientImpl {

        private static int port = 8088;

        public static void sayHello() throws Exception {
             
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
            GreetingServiceGrpc.GreetingServiceFutureStub stub = GreetingServiceGrpc
                    .newFutureStub(channel)
                    .withCallCredentials(new ClientTokenCredentials("mytoken"));
            HelloRequest req = HelloRequest.newBuilder().setName("Bikash").build();
            HelloResponse resp = stub.sayHello(req).get();
            System.err.println(resp.getGreeting());
        }
    }
}
