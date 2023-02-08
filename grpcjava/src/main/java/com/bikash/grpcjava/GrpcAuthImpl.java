/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bikash.grpcjava;

import io.grpc.Context;
import io.grpc.Contexts;
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
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bikash
 */
public class GrpcAuthImpl {

    public static void grpcServer() {
        
        try {
            Server server = ServerBuilder
                    .forPort(8088)
                    .addService(new GreetingServiceImpl())
                    .intercept(new AuthorizationServerInterceptor())
                    .build();
            server.start();
            System.out.println("Server started. Listening to " + server.getPort());
            server.awaitTermination();
        } catch (Exception ex) {
            Logger.getLogger(GrpcAuthImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class Constants {

        public static final String JWT_SIGNING_KEY = "L8hHXsaQOUjk5rg7XPGv4eL36anlCrkMz8CJ0i/8E/0=";
        public static final String BEARER_TYPE = "Bearer";

        public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
        public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

        private Constants() {
            throw new AssertionError();
        }
    }

    public static class AuthorizationServerInterceptor implements ServerInterceptor {

        private JwtParser parser = Jwts.parser().setSigningKey(GrpcAuthImpl.Constants.JWT_SIGNING_KEY);

        @Override
        public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
            String value = metadata.get(GrpcAuthImpl.Constants.AUTHORIZATION_METADATA_KEY);

            Status status;
            if (value == null) {
                status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
            } else if (!value.startsWith(GrpcAuthImpl.Constants.BEARER_TYPE)) {
                status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
            } else {
                try {
                    String token = value.substring(GrpcAuthImpl.Constants.BEARER_TYPE.length()).trim();
                    Jws<Claims> claims = parser.parseClaimsJws(token);
                    Context ctx = Context.current().withValue(GrpcAuthImpl.Constants.CLIENT_ID_CONTEXT_KEY, claims.getBody().getSubject());
                    return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
                } catch (Exception e) {
                    status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
                }
            }

            serverCall.close(status, metadata);
            return new ServerCall.Listener<>() {
                // noop
            };
        }
    }

}
