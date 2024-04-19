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
          
        </div>
    </li>
  </ul>
</div>
<h2>How to use bottomsheetdialogfragment</h1>
<div>
  <h5>1)n activity</h5>
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
