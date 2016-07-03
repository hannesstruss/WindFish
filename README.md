# WindFish :whale:

*"Why? I have no idea, I'm just a kid!"*

WindFish is a library for Android developers that will keep your device awake while
your app is in focus, and lets you toggle that behavior with a [Quick Settings Tile][help-video]
(requires Android N).

Unlike the *Stay awake* developer setting, you don't have to turn off WindFish at the end of the day.
No more digging deep in settings, no more brightly lit phones on your nightstand so far away from your bed.

## How to use

First, install the [companion app (APK)][companion] which includes the Quick Settings Tile.

Then, add the library to your project:

```groovy
dependencies {
  debugCompile 'de.hannesstruss.windfish:windfish-library:+'
}

```

```java
public class MyApplication extends Application {
  @Override public void onCreate() {
    super.onCreate();

    if (BuildConfig.DEBUG) {
      WindFish.install(this);
    }
  }
}
```

And that's it! See [the video of WindFish in action. :tv:][help-video]

## How it works

The [companion app][companion], that also contains the Quick Settings Tile, exports
a service that the library binds to. When the Tile is activated, the service sends a
message to the library in your app, which calls `getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);`.
That's all there is to it.

## License

    Copyright 2016 Hannes Struss

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [help-video]: https://youtu.be/AuuIB4cT2SA
 [companion]: https://github.com/hannesstruss/WindFish/releases

