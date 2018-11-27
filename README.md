

# Setup


## Install the docgen plugin locally on your machine

1. `git clone git@bitbucket.org:rndrnl/docgen.git`
2. `cd docgen`
3. `./gradlew publishToMavenLocal`


## Run docgen
1. clone this repo
2. open in IntelliJ
3. in the gradle tool panel run: docgen/docgen

**input directory is at:**
- `src/main/kotlin/docs`

**outputs are:**
   - markdown files and docsify setup at `build/docsgen`
   - generated sources for examples are at `src/main/kotlin/examples`


## Run the docsify server to inspect the output

1. you need node and npm installed on your machine
2. cd `build/docgen/docsify/`
3. `npm install`
4. npm run serve
5. visit `http://localhost:3000`

