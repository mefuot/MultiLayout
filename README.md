# MultiLayout
android layout for multi purpose.

- show empty list layout
- show load fail layout
- show loading layout

![preview](capture/preview.gif)

## Install

#### Maven
```groovy
<dependency>
  <groupId>com.pong.library</groupId>
  <artifactId>multi-layout</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

#### Gradle
root build.gradle
```groovy
allprojects {
    repositories {
        jcenter();
    }
}
```

dependency
```groovy
compile 'com.pong.library:multi-layout:1.0.2'
```

## Basic Usage
in Xml
```xml
 <com.pong.library.multilayout.MultiLayout
        android:id="@+id/multi_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:empty_msg:.......
        app:loading_msg:.......
        app:error_msg:.......
        app:loading_indicator:......./>
```
| attr | description |
|:---|:---|
| empty_msg | set empty message. Default is "No item found" |
| loading_msg | set loading message. Default is "Loading..." |
| error_msg | set failed message. Default is "Can't load data. Please try again." |
| loading_indicator |  set loading indicator drawable (Optional) |

in Java
```java
import com.excelbkk.pong.multilayout.MultiLayout;
import com.excelbkk.pong.multilayout.OnRetryListener;

public class MainActivity extends AppCompatActivity {

    private MultiLayout multiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...

        multiLayout = (MultiLayout) findViewById(R.id.multi_layout);
        
        //remove loading/empty/fail view.
        multiLayout.removeAllViews();
        
        //Show loading view
        multiLayout.showLoading();
        
        //Show empty item view
        multiLayout.showEmpty();
        
        //Show failed view
        multiLayout.showFail();'
        
        //Show failed view with retry button
        multiLayout.showFail(new OnRetryListener() {
            @Override
                public void onRetry() {
                    Toast.makeText(MainActivity.this,"Retry!",Toast.LENGTH_SHORT).show();
                }
            });
            
        ...
    }
```

## Customization

Change message of each view
```java
  public void setEmptyMessage(@NonNull String msg)

  public void setFailMessage(@NonNull String msg)

  public void setLoadingMessage(@NonNull String msg)
    
  public void setRetryButtonTitle(@NonNull String title)
  
```

Change retry and loading drawable
```java
  public void setRetryButtonBackgroundDrawable(@NonNull Drawable drawable)
  
  public void setRetryButtonBackgroundColor(int color)
  
  public void setLoadingDrawable(Drawable drawable)
  
  public void setLoadingOrientation(int orientation) 
  // sample multiLayout.setLoadingOrientation(LinearLayout.VERTICAL);
```

Change message size and color with TextOption
```java
  ...
  
  public void showMyFailedView(){
  
    // change message size to 20sp and color to blue. (default is 16sp and Color.DKGRAY)
    TextOption messageOption = new TextOption(20, Color.Blue); 
    multiLayout.setFailMessage("Oh. Something wrong!!", messageOption);
    
    // change button size to 36sp and color to red. (default is 16sp and Color.WHITE)
    TextOption buttonOption = new TextOption(36, Color.Red); 
    multiLayout.setRetryButtonTitle("Do it again!", buttonOption);
    
    multiLayout.showFail(new OnRetryListener() {
            ...
            ...
      });
  }
  
  ...
```

## License
<pre>
Copyright 2017 mefuot

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>
