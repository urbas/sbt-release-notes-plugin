# SBT Release Notes Plugin

[![Build Status](https://travis-ci.org/urbas/sbt-release-notes-plugin.svg?branch=master)](https://travis-ci.org/urbas/sbt-release-notes-plugin)

This SBT plugin generates release notes. It takes multiple small release note entry files and concatenates them into
a complete release notes file. The small release note entry files can be added by developers for their isolated
features. Such a separation of per-feature release notes is useful in automatic release notes generation when performing
automated releases (e.g.: releases with continuous integration).

## Usage

1. Add this to your `project/plugins.sbt`:

  ```scala
  addSbtPlugin("si.urbas" % "sbt-release-notes-plugin" % "0.0.1")
  ```

2. Choose your [release notes format](#formats).

3. __Optional__: Choose your [release notes strategy](#strategies).

4. Put the selected format and strategy into your project. Add the following to your `build.sbt` file:

  ```scala
  val root = project.in(".")
    .enablePlugins(<Selected format>, <Selected strategy>)
  ```

An example configuration suitable for GitHub repositories:

  ```scala
  val root = project.in(".")
    .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy)
  ```

Now you can finally run the following SBT commands.

## SBT tasks

`releaseNotes`: Creates the release notes file and puts it in a location determined by the selected format and strategy.

`blessReleaseNotes`: Prepares release notes for the next version. You must commit the changes made by this task.

### Formats

__Markdown__:

- Format name: `MdReleaseNotesFormat`
- Release note entries: `src/releasenotes/*.md`
- Default release notes location: `target/releasenotes/RELEASE_NOTES.md`

### Strategies

__Root folder__ (suitable for GitHub-style repositories):

- Strategy name: `RootFolderReleaseNotesStrategy`

Places the release notes file into the build's root folder.