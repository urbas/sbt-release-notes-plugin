## 0.0.2

- __New feature__: Added `HeaderlessReleaseNotesStrategy`.

- __New feature__: Added `GitHubReleaseNotesStrategy`.

- __New feature__: Grouping of release notes entries. Just add `enablePlugins(GroupReleaseNotesByFirstLine)` to your `build.sbt`.

- __API break__: All strategies have been moved into the `si.urbas.sbt.releasenotes.strategies` package.

- __API break__: All formats have been moved into the `si.urbas.sbt.releasenotes.formats` package.

- __Bugfix__: The Sphinx strategy now correctly cleans the release notes file.

## 0.0.1

- __Feature__: Added support for Markdown release notes.
- __Feature__: Added support for RST release notes.
- __Feature__: Added support for release notes blessing in the root folder.
- __Feature__: Added support for release notes in Sphinx.
