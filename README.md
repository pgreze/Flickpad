# Flickpad

## Description

A simple photo viewer application.

![Demo](https://user-images.githubusercontent.com/14812354/54864917-45f0e780-4da1-11e9-8799-7078734687da.gif)

Please see [Release 1.0](https://github.com/pgreze/Flickpad/releases/tag/1.0) description for a full description, screenshots and download link of this project.

## Technical description

This app uses an simple MVP (Model-View-Presenter) architecture.

- app package: containing Android's Application class and initial configuration. Notice [Dagger](https://github.com/google/dagger) usage as Dependency Injection Container.
- data package: **Model** layer consuming Flickr REST API using [Retrofit](https://square.github.io/retrofit/), [OkHttp](http://square.github.io/okhttp/) and [Gson](https://github.com/google/gson).
- domain package: convert data model (Flickr objects) to app custom objects, heavily using [RxJava](https://github.com/ReactiveX/RxJava) and [AutoValue](https://github.com/google/auto).
- ui package: containing **View** (MainActivity and fragments) and **Presenter** (easily unit testeable view logic) classes. Notice usage of [Butterknife](https://jakewharton.github.io/butterknife/) and [Picasso](https://square.github.io/picasso/) for image processing.

Additional credits:

- [Stetho](http://facebook.github.io/stetho/) allowing to debug Android app via [chrome://inspect](chrome://inspect).
- [Crashlytics](https://fabric.io/kits/android/crashlytics) bug report tool.

## Build

Please create a file app/gradle.properties with this content:

```
FLICKR_CONSUMER_KEY=here_your_flickr_key
FLICKR_CONSUMER_SECRET=here_your_flickr_secret
RELEASE_SIGNING_PASSWORD=here_your_release_keystore_password
```

And use:

```
./gradlew assembleRelease
```
