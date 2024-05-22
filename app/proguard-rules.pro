## Add project specific ProGuard rules here.

# Don't obfuscate so that Crashlytics reports and logcat errors can be read
# https://firebase.google.com/docs/crashlytics/get-deobfuscated-reports?platform=android
-dontobfuscate
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.

#https://r8.googlesource.com/r8/+/refs/heads/master/compatibility-faq.md

#---------------Begin: proguard configuration for Retrofit  ----------
# https://github.com/square/retrofit/issues/3751
 # Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
 ##---------------End: proguard configuration for Retrofit  ----------

 # *** Application specific classes that will be serialized/deserialized ***
 -keep class com.example.template.** { <fields>; } # Keep everything in project
