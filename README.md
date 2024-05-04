## Libraries support

- [RoundedImageView](https://github.com/vinc3m1/RoundedImageView)
- `implementation ("com.facebook.shimmer:shimmer:0.1.0@aar")`
- `implementation ("com.github.bumptech.glide:glide:4.16.0")`

## Details BottomSheetDialogFragment

- **Idea:** General use for screens
- **Solution:** Change the fragment when clicking on the status button of the bottomsheet dialog that needs to be displayed

### Perform

- **Step 1:** Create layout `bottomsheetfragment.xml`
- **Step 2:** Create class `MyBottomSheerDialogFragment` extends `BottomSheetDialogFragment`
- **Step 3:** Override method `onCreateView` and inflate layout `bottomsheetfragment.xml`
- **Step 4:** Override method `onViewCreated` and set fragment. Change fragment in `onViewCreated` because it's called multiple times in the lifecycle while `onCreateView` is called only once.
  ```java
  if (fragment != null){
      fragment = getFragment();
      FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.fragment,fragment).commit();
  }

## How to use bottomsheetdialogfragment
### 1) Use in activity
- **Step 1:** Create a new instance: `MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();`
- **Step 2:** Set the fragment to show in the bottomsheet
  - Example: `dialogFragment.setFragment(new MyJobFragment());`
- **Step 3:** Show the bottomsheet: `dialogFragment.show(getSupportFragmentManager(), dialogFragment.getTag());`
- Add this attribute in your application's manifest: `android:networkSecurityConfig="@xml/network_security_config"`

## How to Create Loading with Library "com.facebook.shimmer:shimmer:0.1.0@aar"

Please refer to the code commit "init loading check with status internet".

## Deploy API in Emulator

- **Step 1:**. Create `res/xml/network_security_config.xml`:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

- **Step 2:**. Add `android:networkSecurityConfig="@xml/network_security_config"`
- **Step 3:**. Use host: 10.0.2.2:[port]



