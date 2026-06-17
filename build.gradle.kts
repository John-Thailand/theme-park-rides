// Antというビルドツールが持っているReplaceTokensを利用する
// Welcome to @THEME_PARK_NAME@ を Welcome to Grelephant's Wonder Worldのように置換できる
import org.apache.tools.ant.filters.ReplaceTokens

// baseプラグインを適用している
// clean、assemble、buildなどの基本的なタスクを追加する
// gradle cleanでbuildフォルダを削除する
plugins {
  base
}

// プロジェクトのプロパティを設定
description = "Theme park build automation"
group = "com.grelephant"
version = "1.0-SNAPSHOT"

// Taskを登録している
// タスク名はgenerateDescriptions
// Copyクラスという設計図からgenerateDescriptionsという実体を作る
tasks.register<Copy>("generateDescriptions") {
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
}

tasks.register<Zip>("zipDescriptions") {
  // build/descriptions/をZipに含める
  from(layout.buildDirectory.dir("descriptions"))
  // build/をZipファイルの出力先にする
  destinationDirectory.set(layout.buildDirectory)
  // 作成されるZipファイル名
  archiveFileName.set("descriptions.zip")
}

tasks.register("sayHello") {
  doLast {
    println("Hello")
  }
}
