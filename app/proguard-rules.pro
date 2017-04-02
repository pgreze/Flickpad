## Retrolambda https://github.com/evant/gradle-retrolambda
-dontwarn java.lang.invoke.*

## Retrofit http://square.github.io/retrofit/
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# Square libs
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

# Support lib v7
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# App
-keep class fr.pgreze.flickpad.data.flickr.model
