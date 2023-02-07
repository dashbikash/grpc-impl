import grpc
import Greeting_pb2 as pb2
import Greeting_pb2_grpc  as pb2_grpc 

def grpc_client():
    channel= grpc.insecure_channel("localhost:8090")
    stub=pb2_grpc.GreetingServiceStub(channel)
    resp=stub.SayHello(pb2.HelloRequest(name="Bikash"))
    print(resp)

grpc_client()


