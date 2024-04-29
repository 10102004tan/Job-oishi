
   <h2>Libraries support</h2>
  <ul>
  <li> https://github.com/vinc3m1/RoundedImageView</li>
  <li> implementation ("com.facebook.shimmer:shimmer:0.1.0@aar")</li>
    <li>implementation ("com.github.bumptech.glide:glide:4.16.0")</li>
</ul>
</div>
<div>
  <h2>Details BottomSheetDiaLogFragment</h2>
  <ul>
    <li> Idea :  
     - General use for screens
    </li>
    <li>
      Solution : Change the fragment when clicking on the status button of the bottomsheet dialog that needs to be displayed
    </li>
    <li>
       <h5> Perform</h5>
        <div>
          <ul>
            <li>
              Step1 : Create layout bottomsheetfragment.xml
            </li>
            <li>
              Step2 : Create class MyBottomSheerDialogFragment extends BottomSheetDialogFragment
            </li>
            <li>
              Step 3 : 
              Overide method : onCreateView and inflate layout bottomsheetfragment.xml
            </li>
            <li>Step 4 : 
              Overide method  : onViewCreated and set fragment on . Change fragment in onViewCreated because nó được gọi nhiều lần trong vòng đời trong khi onViewCreate chỉ được gọi 1 lần trong vòng đời
              <div>
                 if (fragment != null){ <br>
                fragment = getFragment();<br>
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();<br>
                fragmentTransaction.replace(R.id.fragment,fragment).commit();<br>
              }
              </div>
            </li>
          </ul>
        </div>
    </li>
  </ul>
</div>
<h2>How to use bottomsheetdialogfragment</h1>
<div>
  <h5>1)Use  activity</h5>
  <ul>
    <li>Step 1 : MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();</li>
    <li>Step 2 : Set fragmet want show in bottomsheet 
      <div>
        Exemple :  dialogFragment.setFragment(new MyJobFragment());
      </div>
    </li>
    <li>Step 3 : Show bottomsheet : dialogFragment.show(getSupportFragmentManager(),dialogFragment.getTag());</li>
  </ul>
    </div>
  </li>
  <li>
    
    add attribute in application mainifest : 
      android:networkSecurityConfig="@xml/network_security_config"
      
  </li>

<div>
  <h2>Result : </h2>
  <a href="https://drive.google.com/file/d/1xWluv_G0jWedfrp3M5kcE3mWQvATOa5t/view?usp=sharing">See now ...</a>
</div>

<div>
  <h2>How to create loading with  library "com.facebook.shimmer:shimmer:0.1.0@aar"</h2>
  Pleae see code commit "init loading check with status internet"
</div>


#Deploy api in emulator
<h2>Quiz start</h2>
<uL>
  <li>
    <div>
      
      Create res/xml/network_security_config.xml
      <?xml version="1.0" encoding="utf-8"?>
            <network-security-config>
            <domain-config cleartextTrafficPermitted="true">
            <domain includeSubdomains="true">10.0.2.2</domain>
          </domain-config>
      </network-security-config> 

<div>
  <li>
    Use host : 10.0.2.2:[port] :)))
  </li>
</uL>
