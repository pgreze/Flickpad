# Flickpad

## Description

Photo viewer application sample.

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
