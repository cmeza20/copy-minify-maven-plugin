# Copy and minify css/js

- Copy files/folders and minify(css, js)

## Features ##
* Copy files
* Copy folders
* Minify js
* Minify css

## Dependencies ##

* **Java 8**

## Maven Plugin ##
Download the jar through Maven:
```xml
<build>
	<plugins>
		<plugin>
			<groupId>com.cmeza</groupId>
			<artifactId>copy-minify-maven-plugin</artifactId>
			<version>1.0.0</version>
			<configuration>
                <outputFolder>/home/deploy</outputFolder>
                <copy>
                    <target>
                        <destinationFolder>wars</destinationFolder>
                        <searchIn>app-web/target</searchIn>
                        <suffix>.war</suffix>
                        <findInParent>true</findInParent>
                    </target>
                    <target>
                        <destinationFolder>wars</destinationFolder>
                        <searchIn>app-api/target</searchIn>
                        <suffix>.war</suffix>
                        <findInParent>true</findInParent>
                    </target>
                    <target>
                        <destinationFolder>properties</destinationFolder>
                        <searchIn>src/main/resources</searchIn>
                        <suffix>.yml</suffix>
                        <prefix>application-</prefix>
                        <findInParent>false</findInParent>
                    </target>
                </copy>
                <minify>
                    <bundleConfiguration>src/main/resources/bundles.json</bundleConfiguration>
                    <destinationFolder>minified</destinationFolder>
                    <searchIn>resources/src/main/webapp</searchIn>
                    <findInParent>true</findInParent>
                </minify>
			</configuration>
		</plugin>
	</plugins>
</build>
```

| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| outputFolder |  Yes |  | Output deploy folder |
| copy | No | [target] | List of objectives to copy |
| target.destinationFolder | No | ${outputFolder} | Destination folder | 
| target.searchIn | No |  | Search the folder or file |
| target.suffix | No |  | File ends with |
| target.prefix | No |  | File starts with |
| target.findInParent | No | false | Search in the upper directory |
| minify | No |  | Configuration to minify |
| minify.bundleConfiguration | Yes |  | Json configuration file path |
| minify.destinationFolder | No | ${outputFolder} | Destination folder |
| minify.searchIn | No | ${rootFolder} | Search the folder |
| minify.findInParent | No | false | Search in the upper directory |

#### Run maven plugin
```
$ mvn copy-minify:deploy
```

## Json configuration example ##

```json
{
  "configuration": {
    "charset": "iso-8859-1",
    "verbose": true,
    "bufferSize": 4096,
    "yuiLineBreak": -1,
    "yuiNoMunge": false,
    "yuiPreserveSemicolons": false,
    "yuiDisableOptimizations": false,
    "closureLanguageIn": "ECMASCRIPT_2021",
    "closureLanguageOut": "ECMASCRIPT5",
    "closureEnvironment": "BROWSER",
    "closureCreateSourceMap": false,
    "closureAngularPass": false,
    "cssEngine": "YUI",
    "jsEngine": "CLOSURE"
  },
  "bundles": [
    {
      "type": "css",
      "name": "css-static-combined.css",
      "files": [
        "css/reset.css",
        "css/fonts.css"
      ]
    },
    {
      "type": "js",
      "name": "js-static-combined.js",
      "files": [
        "js/main.core.js",
        "js/main.search.js"
      ]
    }
  ]
}
```

License
----

MIT
