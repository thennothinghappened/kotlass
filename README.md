
# Kotlass
Unofficial Kotlin Multiplatform client for the [Compass](https://compass.education/) REST API!

## Example Usage

Authenticating and grabbing today's schedule:
```kotlin
fun main() = runBlocking {
    val credentials = CompassUserCredentials(
        domain = "some-school.compass.education",
        userId = 1000,
        cookie = "cookie"
    )

    val client = CompassApiClient(credentials)

    if (client.checkAuthentication() !is CompassApiResult.Success) {
        println("Failed to authenticate!")
        return@runBlocking
    }

    val today = client.getCalendarEvents(LocalDate.now())
    
    if (today !is CompassApiResult.Success) {
        val errorMessage = when (val failure = (today as CompassApiResult.Failure).error) {
            is CompassApiError.HasCause -> failure.error.toString()
            else -> failure.toString()
        }
        
        println("Failed getting today's events: $errorMessage")
        return@runBlocking
    }
    
    val events: List<CalendarEvent> = today.data
        .sortedByDescending { event -> event.start }
    
    events.forEach { event -> 
        println("${event.name}: ${event.start} to ${event.finish} for student ID ${event.targetStudentId}")
    }
}
```

### Usage in an app!
See [Pelorus](https://github.com/thennothinghappened/Pelorus) for an example of using Kotlass in an Android app.

## How???
The bulk of work on Kotlass has been done purely through the F12 Network tab, basically just monitoring what requests
happen when doing what action in the UI, and going from there to figure out what they do. There's a few obscure things
in the API, but overall most things make pretty logical sense looking through the JSON payloads.

Aside from that, I'm now testing running the mobile app through a proxy to, basically, do the same thing there. I
expected the mobile app to use some different endpoints, that might be more or less useful, so combining those in
is helpful to try and get the most versatile uses out.

## Contributing
Kotlass is always open to contributions! I can only test its functionality and document the API for the version and
enabled featureset at my school, so there's pretty sizable chunks missing. If you're interested in helping out, the
easiest way to help is test [Pelorus](https://github.com/thennothinghappened/Pelorus)! This is the only real
implementation of Kotlass in action. Pelorus has a built-in bug reporter - Kotlass issues go right here to the issues
tab when submitted.

If you're interested to get more involved and are familiar with Kotlin and comfortable with your browser's DevTools,
feel free to spend some time there to help flesh out missing parts of Kotlass' API.

## Warning!!
Compass' API is not public, and is undocumented. If you intend to use Kotlass, be aware the possibility that it may
stop working at any point as Compass may make breaking changes to their API (though do not seem to have a history of
doing so in the time working on this project).

### Read the TOS!!!
By using Kotlass you take responsibility for your own usage - please ensure you read the
[Compass Terms of Use](https://policies.compass.education/) before using this library to ensure your usage is within
the acceptable usage of the service. I do not condone usage of Kotlass for the purposes of botting or automation in
any capacity, nor as a tool to find or exploit any vulnerabilities in Compass' API.

### License
Kotlass is licensed under the [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html). This means that forking the project
for your own usage requires that the source code continue to be public - whilst the chance that anyone will actually
use this project is admittedly low, anyone who may modify this library for malicious use is held to requiring the source
be public, thus hopefully decreasing the (already low) chances of anyone doing so. I take no responsibility for any
potential damage caused by any fault in, or usage of this library.

### TL;DR
With the scary legal stuff out of the way (and I hope I wrote that right), my overall point is that, being a
proprietary service this library may break due to unforeseen changes. While I will attempt to keep it up to date for
a reasonable timespan, that isn't guaranteed. Use your best judgement. While I have not seen Compass take any action
against the usage of this library (see [Pelorus](https://github.com/thennothinghappened/Pelorus)), this is untested
ground in terms of whether they would have issue with this. Your account is in your own hands!