# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Greeting.proto
"""Generated protocol buffer code."""
from google.protobuf.internal import builder as _builder
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x0eGreeting.proto\x12\x08greeting\"-\n\x0cHelloRequest\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\x0f\n\x07hobbies\x18\x02 \x03(\t\"!\n\rHelloResponse\x12\x10\n\x08greeting\x18\x01 \x01(\t2N\n\x0fGreetingService\x12;\n\x08SayHello\x12\x16.greeting.HelloRequest\x1a\x17.greeting.HelloResponseBI\n\x1c com.bikash.grpcdemo.serviceB\rGreetingProtoP\x01Z\x18\x62ikash/grpcimpl/greetingb\x06proto3')

_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, globals())
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'Greeting_pb2', globals())
if _descriptor._USE_C_DESCRIPTORS == False:

  DESCRIPTOR._options = None
  DESCRIPTOR._serialized_options = b'\n\034 com.bikash.grpcdemo.serviceB\rGreetingProtoP\001Z\030bikash/grpcimpl/greeting'
  _HELLOREQUEST._serialized_start=28
  _HELLOREQUEST._serialized_end=73
  _HELLORESPONSE._serialized_start=75
  _HELLORESPONSE._serialized_end=108
  _GREETINGSERVICE._serialized_start=110
  _GREETINGSERVICE._serialized_end=188
# @@protoc_insertion_point(module_scope)