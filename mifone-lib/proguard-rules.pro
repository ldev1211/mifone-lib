# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keep public class com.example.mifone_lib.core.Factory{
   *;
}

-keep public interface com.example.mifone_lib.listener.MifoneCoreListener{
    *;
}

-keep public class com.example.mifone_lib.model.other.ConfigMifoneCore{
    *;
}

-keep public class com.example.mifone_lib.model.other.RegistrationState{
    *;
}

-keep public class com.example.mifone_lib.model.other.State{
    *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile