from concurrent import futures
import os
import grpc
import Greeting_pb2 as pb2
import Greeting_pb2_grpc  as pb2_grpc
from greeting_service_impl import GrpcServiceImpl
import sys

def grpc_server():
    port="8089"
    server =grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pb2_grpc.add_GreetingServiceServicer_to_server(GrpcServiceImpl(),server=server)
    server.add_insecure_port("[::]:"+port)
    server.start()
    print("Server started. Listening to %s"%port)
    server.wait_for_termination()

def grpc_client_sayhello():
    channel= grpc.insecure_channel("localhost:8089")
    stub=pb2_grpc.GreetingServiceStub(channel)
    resp=stub.SayHello(pb2.HelloRequest(name="Bikash"))
    print(resp)

def grpc_client_fileupload():
    channel= grpc.insecure_channel("localhost:8088")
    stub=pb2_grpc.GreetingServiceStub(channel)
    image:bytes=None
    with open('../files/image.jpeg',"rb") as f:
        image=f.read()
        f.close()        
    filestat=os.stat('../files/image.jpeg')
    resp=stub.UploadFile(pb2.FileRequest(file=image,ext="jpeg",size=filestat.st_size))    
    print(resp)

if __name__=='__main__':
    if sys.argv[1] == '1':
        grpc_server()
    if sys.argv[1] == '2':
        grpc_client_sayhello()
    if sys.argv[1] == '3':
        grpc_client_fileupload()
    