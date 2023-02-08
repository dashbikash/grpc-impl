from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Optional as _Optional

DESCRIPTOR: _descriptor.FileDescriptor

class FileRequest(_message.Message):
    __slots__ = ["ext", "file", "size"]
    EXT_FIELD_NUMBER: _ClassVar[int]
    FILE_FIELD_NUMBER: _ClassVar[int]
    SIZE_FIELD_NUMBER: _ClassVar[int]
    ext: str
    file: bytes
    size: int
    def __init__(self, file: _Optional[bytes] = ..., size: _Optional[int] = ..., ext: _Optional[str] = ...) -> None: ...

class FileResponse(_message.Message):
    __slots__ = ["name", "uri"]
    NAME_FIELD_NUMBER: _ClassVar[int]
    URI_FIELD_NUMBER: _ClassVar[int]
    name: str
    uri: str
    def __init__(self, name: _Optional[str] = ..., uri: _Optional[str] = ...) -> None: ...

class HelloRequest(_message.Message):
    __slots__ = ["hobbies", "name"]
    HOBBIES_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    hobbies: _containers.RepeatedScalarFieldContainer[str]
    name: str
    def __init__(self, name: _Optional[str] = ..., hobbies: _Optional[_Iterable[str]] = ...) -> None: ...

class HelloResponse(_message.Message):
    __slots__ = ["greeting"]
    GREETING_FIELD_NUMBER: _ClassVar[int]
    greeting: str
    def __init__(self, greeting: _Optional[str] = ...) -> None: ...
