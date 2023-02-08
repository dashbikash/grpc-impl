import grpc
import Greeting_pb2 as pb2
import Greeting_pb2_grpc  as pb2_grpc 
import os

def grpc_client():
    channel= grpc.insecure_channel("localhost:8090")
    stub=pb2_grpc.GreetingServiceStub(channel)
    resp=stub.SayHello(pb2.HelloRequest(name="Bikash"))
    print(resp)

def grpc_file_client():
    channel= grpc.insecure_channel("localhost:8088")
    stub=pb2_grpc.GreetingServiceStub(channel)
    image:bytes=None
    with open('../files/image.jpeg',"rb") as f:
        image=f.read()
        f.close()        
    filestat=os.stat('../files/image.jpeg')
    resp=stub.UploadFile(pb2.FileRequest(file=image,ext="jpeg",size=filestat.st_size))
    
    print(resp)

grpc_file_client()