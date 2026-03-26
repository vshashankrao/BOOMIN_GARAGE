# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class kotlinx.serialization.json.** { kotlinx.serialization.KSerializer serializer(...); }
-keep,includedescriptorclasses class com.boomingarage.**$$serializer { *; }
-keepclassmembers class com.boomingarage.** { *** Companion; }
-keepclasseswithmembers class com.boomingarage.** { kotlinx.serialization.KSerializer serializer(...); }

# Firebase
-keep class com.google.firebase.** { *; }
-keep class dev.gitlive.firebase.** { *; }

# Stripe
-keep class com.stripe.android.** { *; }
