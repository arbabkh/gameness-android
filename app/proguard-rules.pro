-keep class !com.nuevo.gameness.** {*; }

-keepattributes Signature,InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-keepattributes *Annotation*


# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault


# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-keepattributes Signature
-keepattributes *Annotations*

-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-keepclasseswithmembers enum * { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.** { *; }
-keep class com.github.** { *; }
-keep class com.apache.** { *; }
-keep class com.android.** { *; }
-keep class com.junit.** { *; }

-keep class com.nuevo.gameness.data.model.** { *; }
-keep class com.nuevo.gameness.utils.** { *; }

-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keep public class * extends java.lang.Exception
-keepattributes SourceFile,LineNumberTable

-keep public class * implements com.bumptech.glide.module.LibraryGlideModule
-keep public class * implements com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$**{
**[] $VALUES;
public *;
}

-keepclasseswithmembers,allowshrinking class * {
native <methods>;
}
# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>