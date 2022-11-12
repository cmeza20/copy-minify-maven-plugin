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
			<version>2.0.0</version>
			<configuration>
                <outputFolder>/home/deploy</outputFolder>
                <versioned>true</versioned>
                <tasks>
                    <task>
                        <type>exec</type>
                        <name>Exec name</name>
                        <bundleConfiguration>src/main/resources/exec.json</bundleConfiguration>
                    </task>
                    <task>
                        <type>copy</type>
                        <name>Copy name</name>
                        <bundleConfiguration>src/main/resources/copy.json</bundleConfiguration>
                    </task>
                    <task>
                        <type>minify</type>
                        <name>Minify name</name>
                        <bundleConfiguration>src/main/resources/minify.json</bundleConfiguration>
                        <destinationFolder>minified</destinationFolder>
                        <searchIn>resources/src/main/webapp</searchIn>
                        <findInParent>true</findInParent>
                    </task>
                </tasks>
			</configuration>
		</plugin>
	</plugins>
</build>
```

| Attribute                  | Required | Default | Description                                                  |
|----------------------------|:--------:|:------:|--------------------------------------------------------------|
| outputFolder               |   Yes    |  | Output deploy folder                                         |
| versioned                  |    No    | true | Create a folder with the following format: yyyy-MM-dd-HHmmss |
| tasks                      |   Yes    | | Things to do                                                 |
| **Exec**                   ||||
| type                       |   Yes    |  | Configuration object for exec                                |
| name                       |    No    |  | Name of task                                                 |
| bundleConfiguration        |    No    |  | Json configuration file path                                 |
| **Copy**                   ||||
| type                       |   Yes    |  | Configuration object for copy                                |
| name                       |    No    |  | Name of task                                                 |
| bundleConfiguration        |    No    |  | Json configuration file path                                 |
| **Minify**                 ||||
| type                       |   Yes    |  | Configuration object for minify                              |
| name                       |    No    |  | Name of task                                                 |
| bundleConfiguration |    No    |  | Json configuration file path                                 |
| destinationFolder   |    No    | ${outputFolder} | Destination folder                                           |
| searchIn            |    No    | ${rootFolder} | Search the folder                                            |
| findInParent        |    No    | false | Search in the upper directory                                |

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
      "findInParent": true,
      "override": true
    },
    {
      "destinationFolder": "wars",
      "searchIn": "app-api/target",
      "suffix": ".war",
      "findInParent": true,
      "override": true
    },
    {
      "destinationFolder": "resources",
      "searchIn": "resources/src/main/webapp",
      "findInParent": true,
      "override": false
    },
    {
      "destinationFolder": "properties",
      "searchIn": "resources/src/main/resources",
      "suffix": "-prod.yml",
      "findInParent": true,
      "override": false
    }
  ]
}

```
| Attribute                | Required |     Default     | Description                            |
|--------------------------|:-------------:|:---------------:|----------------------------------------|
| bundles                  | No |    [bundle]     | Configuration object for copy [bundle] |
| bundle.destinationFolder | No | ${outputFolder} | Destination folder                     | 
| bundle.searchIn          | No |                 | Search the folder or file              |
| bundle.suffix            | No |                 | File ends with                         |
| bundle.prefix            | No |                 | File starts with                       |
| bundle.findInParent      | No |      false      | Search in the upper directory          |
| bundle.override           | No |      false      | Override file                          |

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


### Run maven plugin
```
$ mvn copy-minify:deploy
```

License
----

MIT
