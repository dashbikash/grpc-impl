/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.bikash.grpcjava;


/**
 *
 * @author bikash
 */
public class Main {


    public static void main(String[] args) throws Exception {
        System.out.println("Hello GRPC Service");
        //GrpcClientImpl.fileUpload();
        GrpcAuthImpl.grpcServer();
    }

}
