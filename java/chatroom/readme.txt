chat room:

implements by blocking i/o
1.0:
    server:
        server accept message, and then broadcast all connected client.
    
    client:
        client connect server, send message to server by line.
        
1.1:
    server:
        input chat room channel and password to connect different room, and
        broadcast specific channel.
        
    client:
        input room id(channel) and password to login specific room.

implements by non-blocking i/o
1.2:
    the same as 1.0
    
1.3:
    the same as 1.1
    
    
    