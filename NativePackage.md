# Native Packaging

To create the native packages/executables this project uses the: **javafx-maven-plugin** maven plugin.

https://github.com/javafx-maven-plugin/javafx-maven-plugin

## Create a native package

### Linux

#### Requirements

For DEB packges: **dpkg-deb** must be installed.

For RPM packages: **rpmbuild** must be installed.

#### Building

Build the native package with:

```bash
  mvn jfx:native
```

Afterwards the native executable is under: **target/jfx/native/stare-1.0-SNAPSHOT/stare-1.0-SNAPSHOT**

To start it:

```bash
  cd target/jfx/native/stare-1.0-SNAPSHOT
  ./stare-1.0-SNAPSHOT 
```

#### DEB and RPM

The installable packges for rpm and deb are found under **target/jfx/native**

* stare-1.0-snapshot-1.0-1.x86_64.rpm
* stare-1.0-snapshot-1.0.deb

### Windows

TBD

### Mac

TBD