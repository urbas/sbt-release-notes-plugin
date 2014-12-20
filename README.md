# SBT Release Notes Plugin

Please see [the release notes](RELEASE_NOTES.md) for the latest version of this plugin.

[![Build Status](https://travis-ci.org/urbas/sbt-release-notes-plugin.svg?branch=master)](https://travis-ci.org/urbas/sbt-release-notes-plugin)

This SBT plugin takes multiple small release note entry files and concatenates them into
a complete release notes file. The entry files can be added by developers for their isolated
features. The main use of this plugin is in automated releases (e.g.: releases with continuous integration).

## Usage

1. Add this to your `project/plugins.sbt` (see [latest version](RELEASE_NOTES.md)):

  ```scala
  resolvers += "Sonatype Public Releases" at "https://oss.sonatype.org/content/groups/public/"
  
  addSbtPlugin("si.urbas" % "sbt-release-notes-plugin" % "<LATEST VERSION>")
  ```

2. Add the following to your `build.sbt` file:

  ```scala
  val root = project.in(".").enablePlugins(GitHubReleaseNotesStrategy)
  ```

3. Place release note entries into the `src/releasenotes` folder.

You can now invoke [these SBT tasks](#sbt-tasks).

## Advanced configuration

Instead of choosing the `GitHubReleaseNotesStrategy`, you can create your own by following these steps:

1. Choose your [release notes format](#formats).

2. Choose your [release notes strategies](#strategies).

3. Put the selected format and strategies into your project by add the following to your `build.sbt` file:

  ```scala
  val root = project.in(".")
    .enablePlugins(`<Selected format>`, `<Selected strategy>`)
  ```

  An example configuration suitable for GitHub repositories:

  ```scala
  val root = project.in(".")
    .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy)
  ```

## SBT tasks

`releaseNotes`: Creates the release notes file. The location of the file determined by the [format](#formats)
(by default this file is `target/releasenotes/RELEASE_NOTES`).

`blessReleaseNotes`: Prepares release notes for the next version. You must commit its changes. In detail, this task does
the following:

1. optionally copies the release notes file into the folder determined by the [strategies](#strategies) (default is not
to copy the release notes anywhere).

2. prepends the current release note entries to the `src/releasenotes/releaseNotesBody.previousVersion` file.

3. __DANGER__: deletes all the release notes entry files.

`createGitHubRelease`: Creates a draft release for the current version on GitHub. Follow these instructions to use this task:

1.  Navigate to [https://github.com/settings/applications](https://github.com/settings/applications) and create a "Personal Access Token" (you can name it `sbt-release-notes-plugin`).

2.  Copy the token into the `$HOME/.sbt/.github-release-notes-token` file.

3.  Enable the `si.urbas.sbt.releasenotes.GitHubReleasesPlugin` plugin in your `build.sbt` file:

  ```sbt
  val root = project.in(".").enablePlugins(GitHubReleasesPlugin)
  ```

4.  Place this setting into your `build.sbt` file:

  ```sbt
  gitHubUserName := "your username"

  gitHubRepositoryName := "the name of your repository"
  ```

5.  Invoke `sbt createGitHubRelease`.

### Formats

#### Markdown

- Format name: `MdReleaseNotesFormat`
- Release note entries: `src/releasenotes/*.md`
- Default release notes location: `target/releasenotes/RELEASE_NOTES.md`

#### RST

- Format name: `RstReleaseNotesFormat`
- Release note entries: `src/releasenotes/*.rst`
- Default release notes location: `target/releasenotes/RELEASE_NOTES.rst`

#### Write your own format

Take a look at [the RST](releaseNotesPlugin/src/main/scala/si/urbas/sbt/releasenotes/RstReleaseNotesFormat.scala) or
[Markdown](releaseNotesPlugin/src/main/scala/si/urbas/sbt/releasenotes/formats/MdReleaseNotesFormat.scala) as examples.

### Strategies

#### GitHub

- __Strategy name__: `GitHubReleaseNotesStrategy`

- __Task behaviour__:

  - Task `releaseNotes`: this task produces the `target/releasenotes/RELEASE_NOTES.md` file by prepending the entries from `src/releasenotes` to the `RELEASE_NOTES.md` file (which is in the root folder).

  - Task `blessReleaseNotes`: prepends the entries from `src/releasenotes` directly to the `RELEASE_NOTES.md` file in the root folder. This file should be committed to your VCS.

- __Details__: This strategy uses the [Markdown format](#markdown) and is a composite of [the root folder strategy](#root-folder) and [the headerless strategy](#headerless). We recommend you use this strategy in your GitHub projects.

#### Root folder

- __Strategy name__: `RootFolderReleaseNotesStrategy`

- __Task behaviour__:

  - Task `blessReleaseNotes`: prepends the entries from `src/releasenotes` directly to the blessed release notes file in the root folder.

- __Details__: This strategy places the blessed release notes file into the project's root folder. This strategy can be used in conjunction any format. This strategy is suitable for GitHub-style repositories. See [the github example](samples/github).

#### Sphinx

- __Strategy name__: `SphinxReleaseNotesStrategy`

- __Task behaviour__:

  - Task `releaseNotes`: this task produces the `src/sphinx/releaseNotes.rst`.

- __Details__:

  - This strategy is suitable for use with the [sbt-site plugin](https://github.com/sbt/sbt-site) and its Sphinx support). See [the sphinx example](samples/sphinx).

  - You can add this to your `build.sbt` file, if you want the release notes to be generated during the `makeSite` task before Sphinx generates the documentation:

    ```scala
    import com.typesafe.sbt.site.SphinxSupport._

    generate.in(Sphinx) <<= generate.in(Sphinx).dependsOn(releaseNotes)
    ```

  - You can also use `~ ; releaseNotes ; makeSite` command chain when you're updating release notes.

#### Grouping by first line

- __Strategy name__: `GroupReleaseNotesByFirstLine`

- __Behaviour__: This strategy removes the first line from each release note entry, find all entries that start with the same line, and places them together into the release notes for the current version.

#### Headerless

- __Strategy name__: `HeaderlessReleaseNotesStrategy`

- __Behaviour__: This strategy does not prepend the top header nor does it append the footer to the release notes.