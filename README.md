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

  <h5>2)Use in fragment</h5>
  <ul>
    <li>Step 1 : MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();</li>
    <li>Step 2 : Set fragmet want show in bottomsheet 
      <div>
        Exemple :  dialogFragment.setFragment(new MyJobFragment());
      </div>
    </li>
    <li>Step 3 : dialogFragment.show(getActivity().getSupportFragmentManager(), dialogFragment.getTag());</li>
  </ul>
</div>

<div>
  <h2>Result : </h2>
  <img style="width:400px;height:500px;object-fit:contain" src="https://drive.google.com/file/d/1xWluv_G0jWedfrp3M5kcE3mWQvATOa5t/view?usp=sharing" alt="photo"/>
</div>
