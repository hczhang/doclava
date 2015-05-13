## Introduction ##

This page will guide the development team with setting up their environment to perform a release.


## Prerequisites ##

  * Install/Configure GPG - The artifacts that are deployed to the central maven repositories need to be signed.  To do this you will need to have a public and private keypair.  There is a very good [guide](http://www.sonatype.com/people/2010/01/how-to-generate-pgp-signatures-with-maven/) that will walk you though this.

  * We strongly encourage our developers to install [Maven 3.0.1](http://maven.apache.org/download.html).

## Configuration ##

### Maven ###

As of Maven 2.1.0 you can now encrypt your servers passwords.  We highly recommend that you follow this [guide](http://maven.apache.org/guides/mini/guide-encryption.html) to set your master password and use it to encrypt your Sonatype password in the next section.

### Sonatype ###

Using the instructions from the previous step encrypt your Sonatype password and add the following servers to your `~/.m2/settings.xml` file.  You may already have other servers in this file.  If not just create the file.
```
<?xml version="1.0" encoding="UTF-8"?>
<settings>
    <servers>
        <server>
            <id>sonatype-nexus-snapshots</id>
            <username>simone.tripodi</username>
            <password>{jSMOWnoPFgsHVpMvz5VrIt5kRbzGpI8u+9EF1iFQyJQ=}</password>
        </server>

        <server>
            <id>sonatype-nexus-staging</id>
            <username>simone.tripodi</username>
            <password>{jSMOWnoPFgsHVpMvz5VrIt5kRbzGpI8u+9EF1iFQyJQ=}</password>
        </server>
    </servers>
</settings>
```

### Google Code ###
Using the instructions from the previous step encrypt your Google Code password and add the following server to your `~/.m2/settings.xml` file.  You may already have other servers in this file.  If not just create the file.
```
<?xml version="1.0" encoding="UTF-8"?>
<settings>
    <servers>
        <server>
            <id>googlecode</id>
            <username>simone.tripodi</username>
            <password>{JoOt7XAPsdYHo2uAC2p4bWN7kOVrLglffMw19Z9ETz0=}</password>
        </server>
    </servers>
</settings>
```

## Release ##

The release plugin for maven is already configured in the pom file so all you need to do is execute the following two steps to complete the release.  The first step will create the release tag and update the pom with the correct release and snapshot versions.  The second step will sign and deploy the artifacts to the Sonatype open source repository.  This repository is synced every hour to the central Maven repositories.  If you don't supply the optional gpg.passphrase then you will be prompted for it.

  * Prepare the release
` mvn release:prepare `

  * Perform
` mvn release:perform -Dgpg.passphrase=thephrase`

or just

` mvn release:perform `

and type the gpg passphrase when prompted