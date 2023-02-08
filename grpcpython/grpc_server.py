from concurrent import futures
import grpc
import Greeting_pb2 as pb2
import Greeting_pb2_grpc  as pb2_grpc 

class GrpcServiceImpl(pb2_grpc.GreetingService):
    def SayHello(self,request:pb2.HelloRequest,context):
        resp=pb2.HelloResponse(greeting="Hello from python GRPC, %s"%request.name)
        return resp
    
    def UploadFile(self,request:pb2.FileRequest,context):
        with open("/tmp/python_grpc_image.jpeg", "wb") as f:
            f.write(request.file)
            f.close()
        print(request.size)
        resp=pb2.FileResponse(name="python_grpc_image.jpeg",uri="/tmp/python_grpc_image.jpeg")
        return resp


def grpc_server():
    port="8089"
    server =grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pb2_grpc.add_GreetingServiceServicer_to_server(GrpcServiceImpl(),server=server)
    server.add_insecure_port("[::]:"+port)
    server.start()
    print("Server started. Listening to %s"%port)
    server.wait_for_termination()


grpc_server()