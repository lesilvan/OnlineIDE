# Microcontroller Darkmode Button

### Description

An S32DS project named `tcpclient_2` has been committed to this subdirectory. The code enables the microcontroller to send out an HTML request to the server to toggle darkmode for the online IDE. Additionally a LED toggles on/off each time, when a request to toggle darkmode has been sent.

### Configuration

To configure the server IP and port to send the HTML request from the microcontroller to, edit the macros `REMOTE_IP` and `REMOTE_PORT`, which are provided in `./tcp_client2/Sources/tcp_client.c`
