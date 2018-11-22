The program should have two main functioning modes:
1. The Pitcher (message thrower) should run on the first, „A“, computer, and have the
functionality of generating and sending messages to another computer
2. The Catcher (message catcher) should run on the second, „B“ ,computer and have the
functionality of receiving the message and sending replies to the message received

The parameters will be inserted through a command line and the program should print the
statistics in the console window. The command arguments should be defined as it follows:

-p Pitcher mode

-c Catcher mode

-port <port> [Pitcher] TCP socket port used for connecting
      [Catcher] TCP socket port used for listening
-bind <ip_address> [Catcher] TCP socket bind address that will be used to run listen

-mps    <rate> [Pitcher] the speed of message sending expressed as „messages per second“
        Default: 1

-size   <size> [Pitcher] message length
        Minimum: 50
        Maximum: 3000
        Default: 300

<hostname> [Pitcher] the name of the computer which runs Catcher

Run example:
On computer „compB“: java TCPPing –c –bind 192.168.0.1 –port 9900
On computer „compA“: java TCPPing –p –port 9900 –mps 30 –size 1000 comp B