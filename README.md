# Weather
The Weather App

### Description

The app shows weather in Moscow. It has 2 themes (light and dark).

### How to use it

Tap time in the panel below to see more details of the weather.

# Details of implementation
* Uses cache not to request data from the server more often than once in 10 minutes since previous successful request.
* Works in any orientation (even in half-display mode)
* Does not download any pictures (they are pre-downloaded)

---

`RecycleView` is not used here because the max number of items is constant and small due to the server limitations.
