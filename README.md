# MultiLayout
android layout for multi purpose.

- show empty list layout
- show load fail layout
- show loading layout

## Basic Usage
1. Add MultiLayout component to target xml.
```xml
 <com.excelbkk.pong.multilayout.MultiLayout
        android:id="@+id/multi_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

2. Implement at view
```java
import com.excelbkk.pong.multilayout.MultiLayout;
import com.excelbkk.pong.multilayout.OnRetryListener;

public class MainActivity extends AppCompatActivity {

    private MultiLayout multiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ......

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
            
        ......
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

Custom retry and loading drawable
```java
  public void setRetryButtonBackgroundDrawable(@NonNull Drawable drawable)
  
  public void setRetryButtonBackgroundColor(int color)
  
  public void setLoadingDrawable(Drawable drawable)
```

## License
<pre>
Copyright 2017 Pongpol Pramanpol

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
