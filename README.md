# qlc-rest

QLC-REST is a HTTP REST wrapper around QLC+'s existing WebSocket API.

## Application

I developed this as I wanted a way of controlling DMX lights in my house via my Google Home. 

Although unfortunately Google Home does not support web hooks directly, it can be linked to another service (I used IFTTT), which does. 

This service can then be configured to make a PUT request to a running insnace of this wrapper.

In my case, I set up the wrapper and QLC+ on a Raspberry Pi.

It's worth pointint out that unfortunately IFTTT have now limited users to only 3 applets on the free account. I am currently investigating alternatives.

I do not own an Amazon Alexa but I see no reason why this service couldn't be used in the same way.

## Installation

You will require a Java 11 JDK.

TODO - examples of installation on different platforms?

## Configuration

TODO

## Execution

The application can be started by executing

'''
./gradlew bootRun
'''

This will download all required dependencies, build the application, and start it on port 8080.

## Example calls


