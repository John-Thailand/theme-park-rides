// Antというビルドツールが持っているReplaceTokensを利用する
// Welcome to @THEME_PARK_NAME@ を Welcome to Grelephant's Wonder Worldのように置換できる
import org.apache.tools.ant.filters.ReplaceTokens

// baseプラグインを適用している
// clean、assemble、buildなどの基本的なタスクを追加する
// gradle cleanでbuildフォルダを削除する
plugins {
  java
  base
  id("org.barfuin.gradle.taskinfo") version "2.1.0"
}

// repositories 「依存ライブラリを探しに行く場所」をGradleに教える設定
repositories {
  mavenCentral()
  google()
  maven {
    url = uri("https://my-custom-repo.com")
  }
}

// 必要な依存ライブラリを指定する
// そのライブラリをインストールする際に同時にインストールされるもので不要なライブラリは除外する
dependencies {
  implementation("commons-beanutils:commons-beanutils:1.9.4") {
    exclude(group = "commons-collections", module = "commons-collections")
  }
}

// プロジェクトのプロパティを設定
description = "Theme park build automation"
group = "com.grelephant"
version = "1.0-SNAPSHOT"

// Taskを登録している
// タスク名はgenerateDescriptions
// Copyクラスという設計図からgenerateDescriptionsという実体を作る
val generateDescriptions = tasks.register<Copy>("generateDescriptions") {
  group = "Theme park"
  description = "Generates ride descriptions including token substitution"
  // コピー元
  from("descriptions")
  // コピー先
  // layout.buildDirectoryはbuild/を指します
  // なのでコピー先は build/descriptions/
  into(layout.buildDirectory.dir("descriptions"))
  // ファイルに記載された変数を文字列に置換している
  filter<ReplaceTokens>("tokens" to mapOf("THEME_PARK_NAME" to "Grelephant's Wonder World"))

  doFirst {
    println("Before copy")
  }
  doLast {
    println("After copy")
  }
}

// すでに登録済みのgenerateDescriptionsタスクを後から探して設定を変更している
// tasks.named<Copy>("generateDescriptions") {
//   into(layout.buildDirectory.dir("descriptions-renamed"))
// }

tasks.register<Zip>("zipDescriptions") {
  // build/descriptions/をZipに含める
  from(generateDescriptions)
  // build/をZipファイルの出力先にする
  destinationDirectory.set(layout.buildDirectory)
  // 作成されるZipファイル名
  archiveFileName.set("descriptions.zip")
  // zipDescriptionsを実行するには、まずgenerateDescriptionsを実行する必要があることをGradleが自動的に認識させる
  // dependsOn(generateDescriptions)
  // try catch finallyのfinallyと同様
  finalizedBy(tasks.named("confirmFinished"))
}

tasks.register("sayHello") {
  group = "Theme park"
  description = "Say hello"
  doLast {
    println("Hello")
  }
}

tasks.register("sayBye") {
  doLast {
    println("Bye!")
  }

  // タスクを実行してもスキップするようにする
  // enabled = false

  // 条件に合致すれば処理を実行する
  onlyIf {
    2 == 3 * 2
  }
}

tasks.register("confirmFinished") {
  doLast {
    println("Finito!")
  }
}

// プロジェクト直下のdeletemeというフォルダまたはファイルを削除する
tasks.register<Delete>("deleteme") {
  delete("deleteme")
}

tasks.register("taskB") {
  doLast {
    println(name)
  }
}

tasks.register("taskA") {
  doLast {
    println(name)
  }
  mustRunAfter(tasks.named("taskB"))
}

tasks.register("taskC") {
  dependsOn(tasks.named("taskA"), tasks.named("taskB"))
}
