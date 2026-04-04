CodeOverSight/
├── pom.xml                              # Maven build file
├── .gitignore                          # Ignore target/, .idea/, *.iml
├── README.md                           # Installation & usage
└── src/
└── main/
├── java/
│   └── com/
│       └── CodeOverSight/
│           ├── CodeTriage.java          # Main entry point
│           ├── cli/
│           │   └── ArgsParser.java      # Parse CLI arguments
│           ├── model/
│           │   ├── FileInfo.java         # File metadata container
│           │   └── MethodSig.java        # Method signature container
│           ├── parser/
│           │   └── FileParser.java       # JavaParser wrapper
│           ├── graph/
│           │   └── DotGenerator.java     # Build DOT string
│           └── renderer/
│               └── HtmlRenderer.java      # Wrap DOT in HTML template
└── resources/
└── template.html                   # HTML template with d3-graphviz