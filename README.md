I have a problem using netty.

There is a server, when it started, it will bind to port 8888, and wait clients to connect.

Server and client will send messages to each other:

1. From client to server, it just send a meaning less 'Request' with self client id(can be used to log on server side)
2. From server to client, each time when server get a message from any client, it will try to generate a message with new number to send to all clients

The number is increased by 1 every time, and send to clients by order, so I expect the client side will get them in order.

I mean, for each client, each time it gets a message, the number it contains should just bigger than previous one by 1.

But when I run multi clients to connect the same server, it found the clients will get message in wrong number.

How to reproduce it?
---------------------

1. Import this project on an IDE(Intellij-IDEA, or eclipse)
2. Start the server (Server.scala)
3. Start the client (Client.scala)
4. Check the output of them, it should be OK
5. On terminal, `telnet localhost 8888`, you can see there is some output. Press something and press enter, several times.

You will see there are some errors happen in previous client output, like:

    <client 1419066475018>: 38
    <client 1419066475018>: 39
    <client 1419066475018>: 40
    <client 1419066475018>: 42
    !!! This msg num: 42, pre num: 40
    <client 1419066475018>: 41
    !!! This msg num: 41, pre num: 42
    <client 1419066475018>: 44
    !!! This msg num: 44, pre num: 41
    <client 1419066475018>: 43
    !!! This msg num: 43, pre num: 44
    <client 1419066475018>: 46
    !!! This msg num: 46, pre num: 43
    <client 1419066475018>: 45
    !!! This msg num: 45, pre num: 46
    <client 1419066475018>: 48
    !!! This msg num: 48, pre num: 45
    <client 1419066475018>: 50
    !!! This msg num: 50, pre num: 48
    <client 1419066475018>: 47
    !!! This msg num: 47, pre num: 50
    <client 1419066475018>: 49
    !!! This msg num: 49, pre num: 47
    <client 1419066475018>: 52
    !!! This msg num: 52, pre num: 49
    <client 1419066475018>: 53
    <client 1419066475018>: 54
    <client 1419066475018>: 51
    !!! This msg num: 51, pre num: 54
    <client 1419066475018>: 55
    !!! This msg num: 55, pre num: 51
    <client 1419066475018>: 56
    <client 1419066475018>: 57
    <client 1419066475018>: 58
    <client 1419066475018>: 59

You can see the order is incorrect for some times, then becomes correct finally.

At the same, the output of terminal is not correct neither:

    {"num":45}
    {"num":47}
    {"num":49}
    {"num":51}
    {"num":42}
    {"num":44}
    {"num":46}
    {"num":48}
    {"num":50}
    {"num":52}
    {"num":53}

I'm not sure where is wrong :(

