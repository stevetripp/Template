## Add project specific ProGuard rules here.

# Don't obfuscate so that Crashlytics reports and logcat errors can be read
# https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android
-dontobfuscate
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

#https://r8.googlesource.com/r8/+/refs/heads/master/compatibility-faq.md

# *** Application specific classes that will be serialized/deserialized ***
-keep class com.example.template.** { <fields>; } # Keep everything in project

# This is generated automatically by the Android Gradle plugin.
-dontwarn org.slf4j.impl.StaticLoggerBinder