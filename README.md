# Chat Server

Multi-client TCP chat server and client. Each client runs in a separate thread. Messages are broadcast to all connected clients.

**Author:** Bisman Singh <bismanmadaan1@gmail.com>

## Protocol

- `NAME:username` - Set your display name (send first)
- `MSG:message` - Send a message to all clients

## Build

```bash
make
```

## Run

**Terminal 1 - Start server:**
```bash
make run-server
```

**Terminal 2+ - Start clients:**
```bash
make run-client
```

Or: `java -cp out ChatClient [host] [port]`

Type messages and press Enter. Type `/quit` to exit.
