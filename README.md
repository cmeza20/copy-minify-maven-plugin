# Copy and minify css/js [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.cmeza/copy-minify-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.cmeza/copy-minify-maven-plugin)

- Copy files/folders and minify(css, js)

## Features ##
* Copy files
* Copy folders
* Minify js
* Minify css
* Exec command (Windows/Linux)

## Dependencies ##

* **Java 8**

## Maven Plugin JSON Configuration ##
```xml
<build>
	<plugins>
		<plugin>
			<groupId>com.cmeza</groupId>
			<artifactId>copy-minify-maven-plugin</artifactId>
			<version>1.1.0</version>
			<configuration>
                <outputFolder>/home/deploy</outputFolder>
                <versioned>true</versioned>
                <exec>
                    <bundleConfiguration>src/main/resources/exec.json</bundleConfiguration>
                </exec>
                <copy>
                    <bundleConfiguration>src/main/resources/copy.json</bundleConfiguration>
                </copy>
                <minify>
                    <bundleConfiguration>src/main/resources/minify.json</bundleConfiguration>
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
| versioned |  No | true | Create a folder with the following format: yyyy-MM-dd-HHmmss|
| exec | No |  | Configuration object for exec |
| exec.bundleConfiguration | No |  | Json configuration file path |
| copy | No |  | Configuration object for copy |
| copy.bundleConfiguration | No |  | Json configuration file path |
| minify | No |  | Configuration object for minify |
| minify.bundleConfiguration | No |  | Json configuration file path |
| minify.destinationFolder | No | ${outputFolder} | Destination folder |
| minify.searchIn | No | ${rootFolder} | Search the folder |
| minify.findInParent | No | false | Search in the upper directory |

### Exec configuration example (exec.json)
```json
{
    "configuration": {
        "verbose": false
    },
    "bundles": [
        {
            "searchIn": "",
            "findInParent": true,
            "command": "mvn install"
        }
    ]
}
```
| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| configuration |  No |  | Configuration object for copy |
| configuration.verbose |  No | false | Print the execution process on the screen|
| bundles | No | [bundle] | Configuration object for exec [bundle] |
| bundle.searchIn | No | ${rootFolder} | Search the folder |
| bundle.findInParent | No | false | Search in the upper directory |
| bundle.command | Yes |  | Command to execute |

### Copy configuration example (copy.json)
```json
{
  "bundles": [
    {
      "destinationFolder": "wars",
      "searchIn": "app-web/target",
      "suffix": ".war",
      "findInParent": true
    },
    {
      "destinationFolder": "wars",
      "searchIn": "app-api/target",
      "suffix": ".war",
      "findInParent": true
    },
    {
      "destinationFolder": "resources",
      "searchIn": "resources/src/main/webapp",
      "findInParent": true
    },
    {
      "destinationFolder": "properties",
      "searchIn": "resources/src/main/resources",
      "suffix": "-prod.yml",
      "findInParent": true
    }
  ]
}

```
| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| bundles | No | [bundle] | Configuration object for copy [bundle] |
| bundle.destinationFolder | No | ${outputFolder} | Destination folder | 
| bundle.searchIn | No |  | Search the folder or file |
| bundle.suffix | No |  | File ends with |
| bundle.prefix | No |  | File starts with |
| bundle.findInParent | No | false | Search in the upper directory |

### Minify configuration example (minify.json)
```json
{
  "configuration": {
    "charset": "iso-8859-1",
    "verbose": true,
    "keepMerged": true,
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
| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| configuration | No | | Configuration object for minify |
| configuration.charset | No | iso-8859-1 | Charset to minify |
| configuration.verbose | No | true | Show full path of files on screen |
| configuration.keepMerged | No | true | Keep merged file |
| configuration.bufferSize | No | 4096 | Buffer size for copying files |
| configuration.yuiLineBreak | No | -1 | Indicates newlines to minify YUI (CSS) |
| configuration.yuiNoMunge | No | false | Inherited |
| configuration.yuiPreserveSemicolons | No | false | Preserve semicolons to minify YUI (CSS) |
| configuration.yuiDisableOptimizations | No | false | Disable optimizations YUI (CSS) |
| configuration.closureLanguageIn | No | ECMASCRIPT_2021 | Languaje IN CLOSURE (JS) |
| configuration.closureLanguageOut | No | ECMASCRIPT5 | Languaje OUT CLOSURE (JS) |
| configuration.closureEnvironment | No | BROWSER | Environment CLOSURE (JS) |
| configuration.closureCreateSourceMap | No | false | create a .min.js.map file -> sourceMappingURL |
| configuration.closureAngularPass | No | false | Inherited |
| configuration.cssEngine | No | YUI | Only YUI available |
| configuration.jsEngine | No | CLOSURE | [CLOSURE/YUI] available |
| bundles | No | [bundle] | Configuration object for minify [bundle] |
| bundle.type | Yes |  | [js/css] available | 
| bundle.name | Yes |  | Final file name |
| bundle.files | Yes | [] | List of files to minify |


## Maven Plugin FULL XML Configuration ##
```xml
<build>
	<plugins>
		<plugin>
			...
			<configuration>
                ...
                <exec>
                    <configuration>
                        <verbose>true</verbose>
                    </configuration>
                     <bundles>
                        <bundle>
                            <searchIn></searchIn>
                            <findInParent>true</findInParent>
                            <command>mvn install</command>
                        </bundle>
                    </bundles>
                </exec>
                <copy>
                    <bundles>
                        <bundle>
                            <destinationFolder>wars</destinationFolder>
                            <searchIn>app-web/target</searchIn>
                            <suffix>.war</suffix>
                            <findInParent>true</findInParent>
                        </bundle>
                        <bundle>
                            <destinationFolder>wars</destinationFolder>
                            <searchIn>app-api/target</searchIn>
                            <suffix>.war</suffix>
                            <findInParent>true</findInParent>
                        </bundle>
                        <bundle>
                            <destinationFolder>resources</destinationFolder>
                            <searchIn>resources/src/main/webapp</searchIn>
                            <findInParent>true</findInParent>
                        </bundle>
                        <bundle>
                            <destinationFolder>properties</destinationFolder>
                            <searchIn>resources/src/main/resources</searchIn>
                            <suffix>.yml</suffix>
                            <findInParent>true</findInParent>
                        </bundle>
                    </bundles>
                </copy>
                <minify>
                    <destinationFolder>minified</destinationFolder>
                    <searchIn>resources/src/main/webapp</searchIn>
                    <findInParent>true</findInParent>
                    <configuration>
                        <charset>iso-8859-1</charset>
                        <verbose>true</verbose>
                        <keepMerged>true</keepMerged>
                        <bufferSize>4096</bufferSize>
                        <yuiLineBreak>-1</yuiLineBreak>
                        <yuiNoMunge>false</yuiNoMunge>
                        <yuiPreserveSemicolons>false</yuiPreserveSemicolons>
                        <yuiDisableOptimizations>false</yuiDisableOptimizations>
                        <closureLanguageIn>ECMASCRIPT_2021</closureLanguageIn>
                        <closureLanguageOut>ECMASCRIPT5</closureLanguageOut>
                        <closureEnvironment>BROWSER</closureEnvironment>
                        <closureCreateSourceMap>false</closureCreateSourceMap>
                        <closureAngularPass>false</closureAngularPass>
                        <cssEngine>YUI</cssEngine>
                        <jsEngine>CLOSURE</jsEngine>
                    </configuration>
                    <bundles>
                        <bundle>
                            <type>css</type>
                            <name>css-static-combined.css</name>
                            <files>
                                <file>css/reset.css</file>
                                <file>css/fonts.css</file>
                            </files>
                        </bundle>
                        <bundle>
                            <type>js</type>
                            <name>js-static-combined.js</name>
                            <files>
                                <file>js/main.core.js</file>
                                <file>js/main.search.js</file>
                            </files>
                        </bundle>
                    </bundles>
                </minify>
			</configuration>
		</plugin>
	</plugins>
</build>
```

### Run maven plugin
```
$ mvn copy-minify:deploy
```

License
----

MIT
