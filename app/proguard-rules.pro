# FolioReader rules
-keep class com.folioreader.** { *; }
-dontwarn com.folioreader.**

# PDF Viewer rules  
-keep class com.github.barteksc.pdfviewer.** { *; }
-dontwarn com.github.barteksc.pdfviewer.**

# Room rules
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static volatile *** INSTANCE;
}

# Keep data classes
-keepclassmembers class com.example.ireader.data.model.** {
    <fields>;
    <init>(...);
}