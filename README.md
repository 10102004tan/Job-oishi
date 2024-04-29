#Deploy api in emulator
<h2>Quiz start</h2>
<uL>
  <li>
    <div>
      Create res/xml/network_security_config.xml
      ==
      <?xml version="1.0" encoding="utf-8"?>
            <network-security-config>
            <domain-config cleartextTrafficPermitted="true">
            <domain includeSubdomains="true">10.0.2.2</domain>
          </domain-config>
      </network-security-config>
      ==
    </div>
  </li>
  <li>
    add attribute in application mainifest : 
    ==
      android:networkSecurityConfig="@xml/network_security_config"
    ==
  </li>

  <li>
    Use host : 10.0.2.2:[port] :)))
  </li>
</uL>
