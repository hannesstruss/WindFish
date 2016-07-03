# WindFish :whale:

*"Why? I have no idea, I'm just a kid!"*

WindFish is a library for Android developers that will keep your device awake while
your app is in focus, and lets you toggle that behavior with a [QuickSettings Tile][quicksettings-docs].

Unlike the *Stay awake* developer setting, you don't have to turn off WindFish at the end of the day.

## How to use

First, install the QuickSettings Tile :point_right: [APK][companion].

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

And that's it!

##

 [quicksettings-docs]: https://developer.android.com/reference/android/service/quicksettings/Tile.html
 [companion]: https://github.com/hannesstruss/WindFish/releases

