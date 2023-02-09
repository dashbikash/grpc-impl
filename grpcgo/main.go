package main

import (
	"context"
	"fmt"
	"log"
	"net"
	"os"
	"time"

	pb "bikash/grpcimpl/proto"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
)

func main() {
	fmt.Println("Hello GRPC GO")
	grpc_hello_client()
	//grpc_server()
}

func grpc_hello_client() {
	conn, err := grpc.Dial("localhost:8088", grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalln(err)
	}
	defer conn.Connect()
	c := pb.NewGreetingServiceClient(conn)
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()
	resp, err := c.SayHello(ctx, &pb.HelloRequest{Name: "Bikash@Go"})
	if err != nil {
		log.Fatalln(err)
	}
	fmt.Println(resp.GetGreeting())
}
func grpc_file_client() {
	conn, err := grpc.Dial("localhost:8088", grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		log.Fatalln(err)
	}
	defer conn.Connect()
	c := pb.NewGreetingServiceClient(conn)
	ctx, cancel := context.WithTimeout(context.Background(), time.Second)
	defer cancel()
	f, err := os.Open("../files/image.jpeg")
	if err != nil {
		log.Fatalln(err)
	}
	fi, err := f.Stat()
	if err != nil {
		log.Fatalln(err)
	}
	fbyte := make([]byte, fi.Size())
	f.Read(fbyte)
	resp, err := c.UploadFile(ctx, &pb.FileRequest{File: fbyte, Ext: "jpeg", Size: 0})
	if err != nil {
		log.Fatalln(err)
	}
	fmt.Println(resp.GetName())
}

// Server Implimentation
type server struct {
	pb.UnimplementedGreetingServiceServer
}

func (s *server) SayHello(ctx context.Context, in *pb.HelloRequest) (*pb.HelloResponse, error) {

	return &pb.HelloResponse{Greeting: "Welcome to Go GRPC, " + in.Name}, nil
}

func grpc_server() {
	listen, err := net.Listen("tcp", fmt.Sprintf(":%d", 8090))
	if err != nil {
		log.Fatalln(err)
	}
	svr := grpc.NewServer()
	pb.RegisterGreetingServiceServer(svr, &server{})
	fmt.Printf("GRPC Server Started. Listening to %d \n", 8090)
	if err := svr.Serve(listen); err != nil {
		log.Fatalln(err)
	}
}
