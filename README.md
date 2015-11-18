TextViews
===================================
Custom Android TextView widgets, including Typeface support

[ ![Download](https://api.bintray.com/packages/indieboyjeff/maven/textviews/images/download.svg) ](https://bintray.com/indieboyjeff/maven/textviews/_latestVersion)

Useage
===================================

1. Add Bintray repository to your build script.

        repositories {
          maven {
            url  "http://dl.bintray.com/indieboyjeff/maven" 
          }
        }
    
2. Add dependencies for TextViews.
        
        dependencies {
            compile(group: 'util.android.textviews', name: 'textviews', version: '1.1', ext: 'aar')
        }

3. Place font files in your project's _assets_ directory

FontTextView
============

You can use a custom font on the view by specifying the name of the font as the

     android:fontFamily
        
attribute in your layout XML.

For example:

    <util.android.textviews.FontTextView
        android:id="@+id/textView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="OpenSans-Regular.ttf"
        android:text="@string/hello_world"
        android:textSize="18sp" />


You can also make use of styles if you wish:

    <style name="FontableStyle" parent="@android:style/TextAppearance">
        <item name="android:textColor">#0320d9</item>
        <item name="android:fontFamily">Audiowide-Regular.ttf</item>
        <item name="android:textSize">18sp</item>
    </style>


ExpandableTextView
==================

As well as supporting custom fonts, as with FontTextView, the ExpandableTextView also supports some additional properties.

    app:expandColor="#1a6b02"
    app:expandText="Reveal More"
    app:trimLength="3" 
    
*trimLength* - how many lines of text should be shown when the view is in it's collapsed state. Default: 4

*expandText* - text to show indicating that a view can be expanded. Default: Show More

*expandColor* - colour of the expandText

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
