/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.bikash.grpcjava;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author bikash
 */
public class Main {


    public static void main(String[] args) {
        System.out.print("Welcome to GRPC Service.");
        String printoption="1. Basic Server\n"
                + "2. Basic Client Call\n"
                + "3. Basic File Upload\n"
                + "4. Token Secured Server\n"
                + "5. Token Secured Client Call\n"
                + "6. JWT Secured Server\n"
                + "7. JWT Secured Client Call\n"
                + "--------------------------\n"
                + "Enter option: ";
        System.out.println(printoption);
        
        try {
            Scanner scanner=new Scanner(System.in);
            switch (scanner.nextInt()) {
                case 1:
                    GrpcBasicImpl.grpcServer();                   
                    break;
                case 2:
                    GrpcBasicImpl.ClientImpl.sayHello();                   
                    break;
                case 3:
                    GrpcBasicImpl.ClientImpl.fileUpload();                   
                    break;
                case 4:
                    GrpcTokenAuthImpl.grpcServer();                   
                    break;
                case 5:
                    GrpcTokenAuthImpl.ClientImpl.sayHello();                   
                    break;
                case 6:
                    GrpcJwtAuthImpl.grpcServer();                   
                    break;
                case 7:
                    GrpcJwtAuthImpl.ClientImpl.sayHello();                   
                    break;
                    
                default:
                    throw new AssertionError();
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
