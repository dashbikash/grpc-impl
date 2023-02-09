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
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bikash
 */
public class GrpcJwtAuthImpl {
     public static void grpcServer() {

        try {
            Server server = ServerBuilder
                    .forPort(8088)
                    .addService(new GreetingServiceImpl())
                    .intercept(new JwtServerInterceptor())
                    .build();
            server.start();
            System.out.println("Server started. Listening to " + server.getPort());
            server.awaitTermination();
        } catch (Exception ex) {
            Logger.getLogger(GrpcTokenAuthImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static final class Constant {

        static final String JWT_SIGNING_KEY = "L8hHXsaQOUjk5rg7XPGv4eL36anlCrkMz8CJ0i/8E/0=";
        static final String BEARER_TYPE = "Bearer";

        static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
        static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

        private Constant() {
            throw new AssertionError();
        }
    }

    public static class JwtServerInterceptor implements ServerInterceptor {

        private JwtParser parser = Jwts.parser().setSigningKey(Constant.JWT_SIGNING_KEY);

        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
            String value = metadata.get(Constant.AUTHORIZATION_METADATA_KEY);

            Status status = Status.OK;
            if (value == null) {
                status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
            } else if (!value.startsWith(Constant.BEARER_TYPE)) {
                status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
            } else {
                Jws<Claims> claims = null;
                // remove authorization type prefix
                String token = value.substring(Constant.BEARER_TYPE.length()).trim();
                try {
                    // verify token signature and parse claims
                    claims = parser.parseClaimsJws(token);
                } catch (JwtException e) {
                    status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
                }
                if (claims != null) {
                    // set client id into current context
                    Context ctx = Context.current()
                            .withValue(Constant.CLIENT_ID_CONTEXT_KEY, claims.getBody().getSubject());
                    Logger.getLogger(this.getClass().getName()).info(claims.getBody().getSubject());
                    return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
                }
            }

            serverCall.close(status, new Metadata());
            return new ServerCall.Listener<ReqT>() {
                // noop
            };
        }

    }

    public static class JwtCredential extends CallCredentials {

        private final String subject;

        public JwtCredential(String subject) {
            this.subject = subject;
        }

        @Override
        public void applyRequestMetadata(final RequestInfo requestInfo, final Executor executor,
                final MetadataApplier metadataApplier) {
            // Make a JWT compact serialized string.
            // This example omits setting the expiration, but a real application should do it.
            final String jwt
                    = Jwts.builder()
                            .setSubject(subject)
                            .signWith(SignatureAlgorithm.HS256, Constant.JWT_SIGNING_KEY)
                            .compact();
            Logger.getLogger(this.getClass().getName()).info(jwt);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Metadata headers = new Metadata();
                        headers.put(Constant.AUTHORIZATION_METADATA_KEY,
                                String.format("%s %s", Constant.BEARER_TYPE, jwt));
                        metadataApplier.apply(headers);
                    } catch (Throwable e) {
                        metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
                    }
                }
            });
        }

        @Override
        public void thisUsesUnstableApi() {
            // noop
        }
    }
    public static class ClientImpl {

        private static int port = 8088;

        public static void sayHello() throws Exception {
            
            ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
            GreetingServiceGrpc.GreetingServiceFutureStub stub = GreetingServiceGrpc
                    .newFutureStub(channel)
                    .withCallCredentials(new GrpcJwtAuthImpl.JwtCredential("bikash"));
            HelloRequest req = HelloRequest.newBuilder().setName("Bikash").build();
            HelloResponse resp = stub.sayHello(req).get();
            System.err.println(resp.getGreeting());
        }
    }
}
