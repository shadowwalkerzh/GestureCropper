/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * aapt tool from the resource data it found.  It
 * should not be modified by hand.
 */

package com.wan.cropper;

public final class R {
    public static final class attr {
        /** <p>Must be an integer value, such as "<code>100</code>".
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
         */
        public static final int aspectRatioX=0x7f010002;
        /** <p>Must be an integer value, such as "<code>100</code>".
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
         */
        public static final int aspectRatioY=0x7f010003;
        /** <p>Must be a boolean value, either "<code>true</code>" or "<code>false</code>".
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
         */
        public static final int fixAspectRatio=0x7f010001;
        /** <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>on</code></td><td>2</td><td></td></tr>
<tr><td><code>onTouch</code></td><td>1</td><td></td></tr>
<tr><td><code>off</code></td><td>0</td><td></td></tr>
</table>
         */
        public static final int guidelines=0x7f010000;
        /** <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
         */
        public static final int imageResource=0x7f010004;
    }
    public static final class color {
        public static final int black_translucent=0x7f050000;
    }
    public static final class dimen {
        /**  Default screen margins, per the Android Design guidelines. 
         */
        public static final int activity_horizontal_margin=0x7f060000;
        public static final int activity_vertical_margin=0x7f060001;
        public static final int textSize=0x7f060002;
    }
    public static final class drawable {
        public static final int ic_launcher=0x7f020000;
        public static final int landscape=0x7f020001;
        public static final int portrait=0x7f020002;
    }
    public static final class id {
        public static final int Button_crop_test=0x7f040007;
        public static final int CropImageViewTest=0x7f040006;
        public static final int CropOverlayView=0x7f040009;
        public static final int ImageView_image=0x7f040008;
        public static final int croppedImage=0x7f040005;
        public static final int landscape_btn=0x7f040004;
        public static final int off=0x7f040002;
        public static final int on=0x7f040000;
        public static final int onTouch=0x7f040001;
        public static final int portrait_btn=0x7f040003;
    }
    public static final class layout {
        public static final int activity_main=0x7f030000;
        public static final int activity_test=0x7f030001;
        public static final int crop_image_view=0x7f030002;
    }
    public static final class string {
        public static final int app_name=0x7f070000;
    }
    public static final class style {
        /** 
        Base application theme, dependent on API level. This theme is replaced
        by AppBaseTheme from res/values-vXX/styles.xml on newer devices.
    

            Theme customizations available in newer API levels can go in
            res/values-vXX/styles.xml, while customizations related to
            backward-compatibility can go here.
        

        Base application theme for API 11+. This theme completely replaces
        AppBaseTheme from res/values/styles.xml on API 11+ devices.
    
 API 11 theme customizations can go here. 

        Base application theme for API 14+. This theme completely replaces
        AppBaseTheme from BOTH res/values/styles.xml and
        res/values-v11/styles.xml on API 14+ devices.
    
 API 14 theme customizations can go here. 
         */
        public static final int AppBaseTheme=0x7f080000;
        /**  Application theme. 
 All customizations that are NOT specific to a particular API-level can go here. 
         */
        public static final int AppTheme=0x7f080001;
        /**  Robo Theme. 
         */
        public static final int RoboTheme=0x7f080002;
    }
    public static final class styleable {
        /** Attributes that can be used with a CropImageView.
           <p>Includes the following attributes:</p>
           <table>
           <colgroup align="left" />
           <colgroup align="left" />
           <tr><th>Attribute</th><th>Description</th></tr>
           <tr><td><code>{@link #CropImageView_aspectRatioX com.wan.cropper:aspectRatioX}</code></td><td></td></tr>
           <tr><td><code>{@link #CropImageView_aspectRatioY com.wan.cropper:aspectRatioY}</code></td><td></td></tr>
           <tr><td><code>{@link #CropImageView_fixAspectRatio com.wan.cropper:fixAspectRatio}</code></td><td></td></tr>
           <tr><td><code>{@link #CropImageView_guidelines com.wan.cropper:guidelines}</code></td><td></td></tr>
           <tr><td><code>{@link #CropImageView_imageResource com.wan.cropper:imageResource}</code></td><td></td></tr>
           </table>
           @see #CropImageView_aspectRatioX
           @see #CropImageView_aspectRatioY
           @see #CropImageView_fixAspectRatio
           @see #CropImageView_guidelines
           @see #CropImageView_imageResource
         */
        public static final int[] CropImageView = {
            0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003,
            0x7f010004
        };
        /**
          <p>This symbol is the offset where the {@link com.wan.cropper.R.attr#aspectRatioX}
          attribute's value can be found in the {@link #CropImageView} array.


          <p>Must be an integer value, such as "<code>100</code>".
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
          @attr name com.wan.cropper:aspectRatioX
        */
        public static final int CropImageView_aspectRatioX = 2;
        /**
          <p>This symbol is the offset where the {@link com.wan.cropper.R.attr#aspectRatioY}
          attribute's value can be found in the {@link #CropImageView} array.


          <p>Must be an integer value, such as "<code>100</code>".
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
          @attr name com.wan.cropper:aspectRatioY
        */
        public static final int CropImageView_aspectRatioY = 3;
        /**
          <p>This symbol is the offset where the {@link com.wan.cropper.R.attr#fixAspectRatio}
          attribute's value can be found in the {@link #CropImageView} array.


          <p>Must be a boolean value, either "<code>true</code>" or "<code>false</code>".
<p>This may also be a reference to a resource (in the form
"<code>@[<i>package</i>:]<i>type</i>:<i>name</i></code>") or
theme attribute (in the form
"<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>")
containing a value of this type.
          @attr name com.wan.cropper:fixAspectRatio
        */
        public static final int CropImageView_fixAspectRatio = 1;
        /**
          <p>This symbol is the offset where the {@link com.wan.cropper.R.attr#guidelines}
          attribute's value can be found in the {@link #CropImageView} array.


          <p>Must be one of the following constant values.</p>
<table>
<colgroup align="left" />
<colgroup align="left" />
<colgroup align="left" />
<tr><th>Constant</th><th>Value</th><th>Description</th></tr>
<tr><td><code>on</code></td><td>2</td><td></td></tr>
<tr><td><code>onTouch</code></td><td>1</td><td></td></tr>
<tr><td><code>off</code></td><td>0</td><td></td></tr>
</table>
          @attr name com.wan.cropper:guidelines
        */
        public static final int CropImageView_guidelines = 0;
        /**
          <p>This symbol is the offset where the {@link com.wan.cropper.R.attr#imageResource}
          attribute's value can be found in the {@link #CropImageView} array.


          <p>Must be a reference to another resource, in the form "<code>@[+][<i>package</i>:]<i>type</i>:<i>name</i></code>"
or to a theme attribute in the form "<code>?[<i>package</i>:][<i>type</i>:]<i>name</i></code>".
          @attr name com.wan.cropper:imageResource
        */
        public static final int CropImageView_imageResource = 4;
    };
}