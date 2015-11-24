TextViews
===================================
Custom Android TextView widgets, including Typeface support

[ ![Download](https://api.bintray.com/packages/indieboyjeff/maven/textviews/images/download.svg) ](https://bintray.com/indieboyjeff/maven/textviews/_latestVersion)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Android](https://img.shields.io/badge/platform-Android-green.svg)](http://developer.android.com)

Usage
===================================

1. Add Bintray repository to your build script.
 ```gradle
 repositories {
     jcenter()
 }
 ```                
 or
 ```gradle                
 repositories {
     maven {
         url  "http://jcenter.bintray.com" 
     }
 }
 ```        
 or
 ```gradle        
 repositories {
     maven {
         url  "http://dl.bintray.com/indieboyjeff/maven" 
     }
 }
 ``` 
2. Add dependencies for TextViews.
 ```gradle
 dependencies {
     compile(group: 'util.android.textviews', name: 'library', version: '1.2.6', ext: 'aar')
 }
 ```        
 or
 ```gradle        
 dependencies {
     compile 'util.android.textviews:library:1.2.6@aar'
 }
 ```
3. Place font files in your project's _assets_ directory

FontTextView
============

You can use a custom font on the view by specifying the name of the font as the

     android:fontFamily
        
attribute in your layout XML.

For example:

```xml
    <util.android.textviews.FontTextView
        android:id="@+id/textView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="OpenSans-Regular.ttf"
        android:text="@string/hello_world"
        android:textSize="18sp" />
```

You can also make use of styles if you wish:

```xml
    <style name="FontableStyle" parent="@android:style/TextAppearance">
        <item name="android:textColor">#0320d9</item>
        <item name="android:fontFamily">Audiowide-Regular.ttf</item>
        <item name="android:textSize">18sp</item>
    </style>
```

If you do not specify the full name of the font file (e.g. font.ttf) then the system will look for the file in the following order
 
 1. Name you specified (e.g. fontName)
 2. Name you specified + .ttf (e.g. fontName.ttf)
 3. Name you specified + .otf (e.g. fontName.otf)


AppCompatFontTextView
=====================

A version of FontTextView derived from AppCompatTextView.


ExpandableTextView
==================

As well as supporting custom fonts, as with FontTextView, the ExpandableTextView also supports some additional properties.

```xml
app:expandColor="#1a6b02"
app:expandText="Reveal More"
app:trimLength="3" 
```
    
* *trimLength* - how many lines of text should be shown when the view is in it's collapsed state. Default: 4

* *expandText* - text to show indicating that a view can be expanded. Default: Show More

* *expandColor* - colour of the expandText

In order to get the expansion text to appear you must remember to set 

```xml
app:ellipsis="end"
```

VerticalTextView
================

This is a simple extension of TextView that displays rotated by 90 degrees.  To change to rotation you need to set the property:

```xml
android:gravity="bottom"
```

FontVerticalTextView
====================
    
This is the same as VerticalTextView but based upon the FontTextView, allowing for custom typefaces.


ShimmerTextView
===============

This is a FontTextView that also supports the _shimmer_ effect. The colour of the reflection can be set by using

```xml
app:reflectionColor="#CCCCCC"
```
    
Other properties of the shimmer animation are controlled through the Shimmer class.  These include:

* Animation duration
* Animation start delay
* Animation repect count
* Delay between repeats of the animation


FontButton
==========

This is a Button based on FontTextView (just as Button is a subclass of TextView).


What fonts?
===================================
Android supports TTF (TrueType) fonts, and later versions support OpenType (OTF) fonts.
Where Android decides it doesn't like a TTF font it tends to substitute Droid Sans without throwing an Exception.


Licence
===================================
        
        Copyright (c) 2015 Jeff Sutton
        
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
        
            http://www.apache.org/licenses/LICENSE-2.0
        
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
<br/> 
