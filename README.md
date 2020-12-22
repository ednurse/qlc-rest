# qlc-rest

QLC-REST is a stand-alone Java application that allows Q Light Controller Plus (QLC+) to be controlled by HTTP calls.

It does this by acting as a wrapper around the existing WebSocket API.

## Application

I developed this as I wanted a way of controlling DMX lights in my house via my Google Home. Although unfortunately Google Home does not support web hooks directly, it can be linked to third-party services that do (I used IFTTT). The third-party service can then be configured to make a PUT request to a running instance of this wrapper.

In my case, I set up the wrapper and QLC+ on a Raspberry Pi, using a static route so this is visible from the outside world. I then use an ethernet to DMX converter to control dimmers etc.

It's worth pointing out that unfortunately IFTTT have now limited users with free accounts to only 3 active applets. I have not yet found a free alternative.

Although I have only tried this with my Google Home, I see no reason why this couldn't be used by any home automation device that can make custom web requests on a given trigger phrase or other action.

## Installation

You will require a Java 8 JDK.

## Configuration

The application can be configured by editing `src/main/resources/application.yml`. This allows you to:

* change the host address of your running QLC+ instance (by default, it assumes QLC+ is running on the same machine)
* change the amount of time it waits for a response (default 500ms)
* change the port that the wrapper listens on (default 9998)

The application will need to be restarted for configuration changes to take effect.

## Execution

The application can be started by executing

```
./gradlew bootRun
```

This will download all required dependencies, build the application, and start it on port 9998.

## Example calls

At the moment, the only thing you can do is control functions. 

### Listing all functions

First you will need to get the function ID. You can do this by making the following request in a browser:

```
http://localhost:9998/functions
```

This will give a response similar to the following:

```
[
    {
        "id": 22,
        "name": "Stop making a scene"
    },
    {
        "id": 23,
        "name": "Chase 1"
    },
    {
        "id": 24,
        "name": Chase 2"
    }
]
```

As far as I'm aware, the ID never changes once you have created a function.

### Setting a function's status

In its simplest form, you can update the status of a function by making a PUT call to the following URL:

```
http://localhost:9998/function/status/{id}/{value}
```

Value is `0` for off and `1` for on. So, for example:

```
http://localhost:9998/function/status/22/1
```

would start the function with the ID 22.

If you would like to set the status of multiple functions in one go, you can make a PUT call to

```
http://localhost/functions/status
```

with a request body similar to the following:

```
[
    {
        "id": 22,
        "value": 0
    },
    {
        "id": 23,
        "value": 0
    },
    {
        "id": 24,
        "value": 1
    }
]
```

This would stop the functions with IDs 22 and 23, and start the function with ID 24.

### Getting a function's status

The current status of any function can be obtained by making a GET request to

```
http://localhost:9998/function/status/{id}
```

## Improvements

Given how basic this application currently is, there are hundreds!

Off the top of my head:

- Add some form of authentication
- Support more than just functions
- Do away with the need to find out a function's ID by accepting a function name parameter instead
